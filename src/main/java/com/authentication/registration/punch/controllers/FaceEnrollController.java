package com.authentication.registration.punch.controllers;

import com.authentication.registration.punch.entities.UserFaceProfile;
import com.authentication.registration.punch.repositories.UserFaceProfileRepository;
import com.authentication.registration.punch.services.FaceRecognitionService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@RequestMapping("/api/face")
public class FaceEnrollController {

    private final FaceRecognitionService faceRecognitionService;
    private final UserFaceProfileRepository faceProfileRepository;

    public FaceEnrollController(FaceRecognitionService faceRecognitionService,
                                UserFaceProfileRepository faceProfileRepository) {
        this.faceRecognitionService = faceRecognitionService;
        this.faceProfileRepository = faceProfileRepository;
    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enroll(@RequestParam Long userId,
                                         @RequestParam MultipartFile image) throws Exception {
        // Convert image to base64
        String base64Image = Base64.getEncoder().encodeToString(image.getBytes());

        // Detect face using Face++ API
        String detectResult = faceRecognitionService.detectFace(base64Image);
        JSONObject detectJson = new JSONObject(detectResult);

        if (!detectJson.has("faces") || detectJson.getJSONArray("faces").length() == 0) {
            return ResponseEntity.badRequest().body("No face detected in the image");
        }

        String faceToken = detectJson.getJSONArray("faces").getJSONObject(0).getString("face_token");

        // Check if user already has a face profile
        UserFaceProfile existingProfile = faceProfileRepository.findByUserId(userId).orElse(null);
        
        if (existingProfile != null) {
            // Update existing profile
            existingProfile.setFaceToken(faceToken);
            faceProfileRepository.save(existingProfile);
        } else {
            // Create new profile
            UserFaceProfile profile = new UserFaceProfile();
            profile.setUserId(userId);
            profile.setFaceToken(faceToken);
            faceProfileRepository.save(profile);
        }

        return ResponseEntity.ok("Face enrolled successfully");
    }
}