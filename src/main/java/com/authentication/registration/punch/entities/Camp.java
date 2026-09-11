package com.authentication.registration.punch.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "camp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Camp {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long campId;

    private Long mmuId;

    private Double campLongitude;
    private Double CampLatitude;

    private String campLocation;

    private LocalDateTime campDate;

    private LocalTime campStartTime;

    private LocalTime campEndTime;
}
