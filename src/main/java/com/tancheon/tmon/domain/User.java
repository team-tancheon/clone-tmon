package com.tancheon.tmon.domain;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

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

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "signup_time", columnDefinition = "TIMESTAMP NULL")
    private ZonedDateTime signupTime;

    @Column(name = "last_signin_time", columnDefinition = "TIMESTAMP NULL")
    private ZonedDateTime lastSigninTime;

    @Column(name = "password_change_time", columnDefinition = "TIMESTAMP NULL")
    private ZonedDateTime passwordChangeTime;

    @Column(name = "email_authorized", nullable = false, columnDefinition = "TINYINT", length = 1)
    private boolean emailAuthorized;

    @Column(name = "email_auth_code")
    private String emailAuthCode;
}
