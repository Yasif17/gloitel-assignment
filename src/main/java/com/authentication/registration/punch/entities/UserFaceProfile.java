package com.authentication.registration.punch.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_face_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFaceProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(columnDefinition = "bytea")
    private byte[] embedding;   // no @Lob — plain bytea, no streaming needed

    @Column(length = 100)
    private String faceToken;   // Face++ API face token for comparison

    // getters/setters
}