package com.tancheon.tmon.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Calendar;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "signup_time")
    private Calendar signupTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "last_signin_time")
    private Calendar lastSigninTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "password_change_time")
    private Calendar passwordChangeTime;

    @Column(name = "email_authorized", nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean emailAuthorized;

    @Column(name = "email_auth_code")
    private String emailAuthCode;
}
