package com.authentication.registration.punch.services;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class FaceRecognitionService {

    @Value("${faceplusplus.api.key}")
    private String apiKey;

    @Value("${faceplusplus.api.secret}")
    private String apiSecret;

    private final OkHttpClient client = new OkHttpClient();
    private static final String API_URL = "https://api-us.faceplusplus.com/facepp/v3";

    public float[] extractEmbedding(BufferedImage bufferedImage) throws Exception {
        throw new UnsupportedOperationException("Use Face++ API directly instead of extracting embeddings");
    }

    public float[] extractEmbedding(String base64Image) throws Exception {
        throw new UnsupportedOperationException("Use Face++ API directly instead of extracting embeddings");
    }

    private BufferedImage decodeBase64ToImage(String base64Image) throws Exception {
        String base64Data = base64Image;
        if (base64Image.contains(",")) {
            base64Data = base64Image.split(",")[1];
        }
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }

    public String detectFace(String base64Image) throws IOException {
        String base64Data = base64Image;
        if (base64Image.contains(",")) {
            base64Data = base64Image.split(",")[1];
        }

        // Resize image if too large to avoid 413 error
        String resizedBase64 = resizeImageIfNeeded(base64Data);

        RequestBody body = new FormBody.Builder()
                .add("api_key", apiKey)
                .add("api_secret", apiSecret)
                .add("image_base64", resizedBase64)
                .build();

        Request request = new Request.Builder()
                .url(API_URL + "/detect")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    private String resizeImageIfNeeded(String base64Image) throws IOException {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            
            // If image is larger than 2MB or dimensions are too big, resize it
            if (imageBytes.length > 2 * 1024 * 1024 || image.getWidth() > 1200 || image.getHeight() > 1200) {
                int maxWidth = 800;
                int maxHeight = 800;
                
                int newWidth = image.getWidth();
                int newHeight = image.getHeight();
                
                if (newWidth > maxWidth) {
                    newHeight = (int) ((double) maxHeight / newWidth * newHeight);
                    newWidth = maxWidth;
                }
                if (newHeight > maxHeight) {
                    newWidth = (int) ((double) maxWidth / newHeight * newWidth);
                    newHeight = maxHeight;
                }
                
                BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                java.awt.Graphics2D g = resizedImage.createGraphics();
                g.drawImage(image, 0, 0, newWidth, newHeight, null);
                g.dispose();
                
                java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                ImageIO.write(resizedImage, "jpg", baos);
                baos.flush();
                
                return Base64.getEncoder().encodeToString(baos.toByteArray());
            }
            
            return base64Image;
        } catch (Exception e) {
            // If resizing fails, return original
            return base64Image;
        }
    }

    public String compareFaces(String faceToken1, String faceToken2) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("api_key", apiKey)
                .add("api_secret", apiSecret)
                .add("face_token1", faceToken1)
                .add("face_token2", faceToken2)
                .build();

        Request request = new Request.Builder()
                .url(API_URL + "/compare")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public double compareFacesFromImages(String base64Image1, String base64Image2) throws Exception {
        String detectResult1 = detectFace(base64Image1);
        JSONObject json1 = new JSONObject(detectResult1);
        
        if (!json1.has("faces") || json1.getJSONArray("faces").length() == 0) {
            throw new Exception("No face detected in first image");
        }
        
        String faceToken1 = json1.getJSONArray("faces").getJSONObject(0).getString("face_token");

        String detectResult2 = detectFace(base64Image2);
        JSONObject json2 = new JSONObject(detectResult2);
        
        if (!json2.has("faces") || json2.getJSONArray("faces").length() == 0) {
            throw new Exception("No face detected in second image");
        }
        
        String faceToken2 = json2.getJSONArray("faces").getJSONObject(0).getString("face_token");

        String compareResult = compareFaces(faceToken1, faceToken2);
        JSONObject compareJson = new JSONObject(compareResult);
        
        if (compareJson.has("confidence")) {
            return compareJson.getDouble("confidence");
        } else {
            throw new Exception("Face comparison failed: " + compareResult);
        }
    }

    public double cosineSimilarity(float[] a, float[] b) {
        throw new UnsupportedOperationException("Use Face++ API compareFaces instead");
    }

    // simple float[] <-> byte[] helpers for DB storage
    public byte[] toBytes(float[] floats) {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(floats.length * 4);
        for (float f : floats) buffer.putFloat(f);
        return buffer.array();
    }

    public float[] toFloatArray(byte[] bytes) {
        java.nio.FloatBuffer floatBuffer = java.nio.ByteBuffer.wrap(bytes).asFloatBuffer();
        float[] floats = new float[floatBuffer.capacity()];
        floatBuffer.get(floats);
        return floats;
    }
}