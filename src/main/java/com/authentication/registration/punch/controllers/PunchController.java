package com.authentication.registration.punch.controllers;

import com.authentication.registration.punch.dtos.requests.PunchInRequest;
import com.authentication.registration.punch.dtos.requests.PunchOutRequest;
import com.authentication.registration.punch.dtos.responses.PunchResponse;
import com.authentication.registration.punch.services.PunchService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@RequestMapping("/api/punch")
public class PunchController {

    private final PunchService punchService;

    public PunchController(PunchService punchService) {
        this.punchService = punchService;
    }

    @PostMapping(value = "/punch-in", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PunchResponse> punchIn(@RequestParam Long userId,
                                                 @RequestParam Long campId,
                                                 @RequestParam Double latitude,
                                                 @RequestParam Double longitude,
                                                 @RequestParam MultipartFile photo) throws Exception {
        String base64Photo = Base64.getEncoder().encodeToString(photo.getBytes());
        PunchInRequest request = new PunchInRequest(userId, campId, latitude, longitude, base64Photo);
        return ResponseEntity.ok(punchService.punchIn(request));
    }

    @PostMapping(value = "/punch-out", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PunchResponse> punchOut(@RequestParam Long userId,
                                                  @RequestParam Long campId,
                                                  @RequestParam Double userLatitude,
                                                  @RequestParam Double userLongitude,
                                                  @RequestParam MultipartFile photo) throws Exception {
        String base64Photo = Base64.getEncoder().encodeToString(photo.getBytes());
        PunchOutRequest request = new PunchOutRequest(userId, campId, userLatitude, userLongitude, base64Photo);
        PunchResponse response = punchService.punchOut(request);
        return ResponseEntity.ok(response);
    }
}