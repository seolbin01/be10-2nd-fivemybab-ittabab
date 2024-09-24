package com.fivemybab.ittabab.user.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String loginId;
    private String pwd;
    private String email;
    private String phone;
    private LocalDate birth;
    private Long courseId;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;
    private LocalDate signUpDate = LocalDate.now();
    private LocalDate signOutDate;

    public void encryptPassword(String encodedPwd) {
        this.pwd = encodedPwd;
    }
}