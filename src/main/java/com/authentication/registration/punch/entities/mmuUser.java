package com.authentication.registration.punch.entities;

import com.authentication.registration.punch.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="mmu_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class mmuUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mmuUserId;

    private Long mmuId;

    private UserStatus userStatus;

}
