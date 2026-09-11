package com.authentication.registration.punch.services.impl;

import com.authentication.registration.punch.dtos.requests.PunchInRequest;
import com.authentication.registration.punch.dtos.requests.PunchOutRequest;
import com.authentication.registration.punch.dtos.responses.PunchResponse;
import com.authentication.registration.punch.dtos.responses.PunchStatusResult;
import com.authentication.registration.punch.entities.Camp;
import com.authentication.registration.punch.entities.PunchRecord;
import com.authentication.registration.punch.entities.UserFaceProfile;
import com.authentication.registration.punch.entities.mmuUser;
import com.authentication.registration.punch.enums.UserStatus;
import com.authentication.registration.punch.exceptions.PunchNotAllowedException;
import com.authentication.registration.punch.exceptions.UserNotActiveException;
import com.authentication.registration.punch.repositories.CampRepository;
import com.authentication.registration.punch.repositories.PunchRepository;
import com.authentication.registration.punch.repositories.UserFaceProfileRepository;
import com.authentication.registration.punch.repositories.mmuUserRepository;
import com.authentication.registration.punch.services.FaceRecognitionService;
import com.authentication.registration.punch.services.GeoFenceService;
import com.authentication.registration.punch.services.PunchService;
import com.authentication.registration.punch.services.PunchTimeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class PunchServiceImpl implements PunchService {

    private final PunchRepository punchRecordRepository;
    private final CampRepository campRepository;
    private final mmuUserRepository mmuUserRepository;
    private final GeoFenceService geoFenceService;
    private final PunchTimeService punchTimeService;
    private final UserFaceProfileRepository userFaceProfileRepository;
    private final FaceRecognitionService faceRecognitionService;

    public PunchServiceImpl(PunchRepository punchRecordRepository,
                            CampRepository campRepository,
                            mmuUserRepository mmuUserRepository,
                            GeoFenceService geoFenceService,
                            PunchTimeService punchTimeService, UserFaceProfileRepository userFaceProfileRepository, FaceRecognitionService faceRecognitionService) {
        this.punchRecordRepository = punchRecordRepository;
        this.campRepository = campRepository;
        this.mmuUserRepository = mmuUserRepository;
        this.geoFenceService = geoFenceService;
        this.punchTimeService = punchTimeService;
        this.userFaceProfileRepository = userFaceProfileRepository;
        this.faceRecognitionService = faceRecognitionService;
    }

    @Transactional
    @Override
    public PunchResponse punchIn(PunchInRequest request) {
        mmuUser user = mmuUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new PunchNotAllowedException("User not found"));

        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new UserNotActiveException("User is not active");
        }

        Camp camp = campRepository.findById(request.getCampId())
                .orElseThrow(() -> new PunchNotAllowedException("Camp not found"));

        // geofence check
        boolean withinGeoFence = geoFenceService.isWithinGeoFence(
                request.getUserLatitude(), request.getUserLongitude(),
                camp.getCampLatitude(), camp.getCampLongitude());

        if (!withinGeoFence) {
            throw new PunchNotAllowedException("You are not within camp location");
        }

        LocalTime now = LocalTime.now();

        // time gate check
        if (!punchTimeService.isWithinTimeWindow(now, camp.getCampStartTime(), camp.getCampEndTime(), true)) {
            throw new PunchNotAllowedException("Camp is closed for punch-in");
        }

        // Face verification using Face++ API
        UserFaceProfile referenceProfile = userFaceProfileRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new PunchNotAllowedException("Face not enrolled for this user"));

        if (referenceProfile.getFaceToken() == null) {
            throw new PunchNotAllowedException("Face not enrolled properly. Please re-enroll.");
        }

        double confidence;
        try {
            // Detect face in punch-in photo
            String detectResult = faceRecognitionService.detectFace(request.getPunchInPhoto());
            org.json.JSONObject detectJson = new org.json.JSONObject(detectResult);
            
            if (!detectJson.has("faces") || detectJson.getJSONArray("faces").length() == 0) {
                throw new PunchNotAllowedException("No face detected in punch-in photo");
            }
            
            String liveFaceToken = detectJson.getJSONArray("faces").getJSONObject(0).getString("face_token");
            
            // Compare with enrolled face token
            String compareResult = faceRecognitionService.compareFaces(referenceProfile.getFaceToken(), liveFaceToken);
            org.json.JSONObject compareJson = new org.json.JSONObject(compareResult);
            
            if (!compareJson.has("confidence")) {
                throw new PunchNotAllowedException("Face comparison failed: " + compareResult);
            }
            
            confidence = compareJson.getDouble("confidence");
            System.out.println("Punch-in face confidence for userId " + request.getUserId() + ": " + confidence);
            
            // Face++ confidence is 0-100, threshold of 70 is reasonable
            if (confidence < 70) {
                throw new PunchNotAllowedException("Face does not match. Confidence: " + confidence);
            }
        } catch (Exception e) {
            throw new PunchNotAllowedException("Face verification failed: " + e.getMessage());
        }

        // prevent duplicate punch-in same day
        punchRecordRepository.findByUserIdAndCampIdAndPunchDate(
                        request.getUserId(), request.getCampId(), LocalDate.now())
                .ifPresent(p -> { throw new PunchNotAllowedException("Already punched in today"); });

        PunchStatusResult statusResult = punchTimeService.calculatePunchStatus(now, camp.getCampStartTime());

        PunchRecord record = new PunchRecord();
        record.setUserId(request.getUserId());
        record.setCampId(request.getCampId());
        record.setPunchDate(LocalDate.now());
        record.setPunchStartTime(now);
        record.setPunchInLatitude(request.getUserLatitude());
        record.setPunchInLongitude(request.getUserLongitude());
        record.setPunchStatus(statusResult.getPunchStatus());
        record.setDeductedMinutes(statusResult.getDeductedMinutes());

        punchRecordRepository.save(record);

        return new PunchResponse(record.getPunchRecordId(), record.getPunchStatus(), "Punch-in successful", now);
    }

    @Transactional
    @Override
    public PunchResponse punchOut(PunchOutRequest request) {
        Camp camp = campRepository.findById(request.getCampId())
                .orElseThrow(() -> new PunchNotAllowedException("Camp not found"));

        PunchRecord record = punchRecordRepository.findByUserIdAndCampIdAndPunchDate(
                        request.getUserId(), request.getCampId(), LocalDate.now())
                .orElseThrow(() -> new PunchNotAllowedException("No punch-in found for today"));

        if (record.getPunchEndTime() != null) {
            throw new PunchNotAllowedException("Already punched out today");
        }

        boolean withinGeoFence = geoFenceService.isWithinGeoFence(
                request.getUserLatitude(), request.getUserLongitude(),
                camp.getCampLatitude(), camp.getCampLongitude());

        if (!withinGeoFence) {
            throw new PunchNotAllowedException("You are not within camp location");
        }

        LocalTime now = LocalTime.now();

        if (!punchTimeService.isWithinTimeWindow(now, camp.getCampStartTime(), camp.getCampEndTime(), false)) {
            throw new PunchNotAllowedException("Cannot punch-out before camp starts");
        }

        // Face verification using Face++ API
        UserFaceProfile referenceProfile = userFaceProfileRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new PunchNotAllowedException("Face not enrolled for this user"));

        if (referenceProfile.getFaceToken() == null) {
            throw new PunchNotAllowedException("Face not enrolled properly. Please re-enroll.");
        }

        double confidence;
        try {
            // Detect face in punch-out photo
            String detectResult = faceRecognitionService.detectFace(request.getPunchOutPhoto());
            org.json.JSONObject detectJson = new org.json.JSONObject(detectResult);
            
            if (!detectJson.has("faces") || detectJson.getJSONArray("faces").length() == 0) {
                throw new PunchNotAllowedException("No face detected in punch-out photo");
            }
            
            String liveFaceToken = detectJson.getJSONArray("faces").getJSONObject(0).getString("face_token");
            
            // Compare with enrolled face token
            String compareResult = faceRecognitionService.compareFaces(referenceProfile.getFaceToken(), liveFaceToken);
            org.json.JSONObject compareJson = new org.json.JSONObject(compareResult);
            
            if (!compareJson.has("confidence")) {
                throw new PunchNotAllowedException("Face comparison failed: " + compareResult);
            }
            
            confidence = compareJson.getDouble("confidence");
            System.out.println("Punch-out face confidence for userId " + request.getUserId() + ": " + confidence);
            
            // Face++ confidence is 0-100, threshold of 70 is reasonable
            if (confidence < 70) {
                throw new PunchNotAllowedException("Face does not match. Confidence: " + confidence);
            }
        } catch (Exception e) {
            throw new PunchNotAllowedException("Face verification failed: " + e.getMessage());
        }

        record.setPunchEndTime(now);
        record.setPunchOutLatitude(request.getUserLatitude());
        record.setPunchOutLongitude(request.getUserLongitude());

        long totalMinutes = Duration.between(record.getPunchStartTime(), now).toMinutes();
        record.setWorkedMinutes(totalMinutes - record.getDeductedMinutes());

        punchRecordRepository.save(record);

        return new PunchResponse(record.getPunchRecordId(), record.getPunchStatus(), "Punch-out successful", now);
    }
}