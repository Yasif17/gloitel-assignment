package com.authentication.registration.punch.entities;


import com.authentication.registration.punch.enums.PunchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "punch_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PunchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long punchRecordId;

    private Long campId;

    private Long userId;

    private LocalDate punchDate;

    private LocalTime punchStartTime;

    private LocalTime punchEndTime;

    private Double punchInLatitude;

    private Double punchInLongitude;

    private String punchInPhoto;

    private Double punchOutLatitude;

    private Double punchOutLongitude;

    private PunchStatus punchStatus;

    private Long deductedMinutes;

    private Long workedMinutes;

}
