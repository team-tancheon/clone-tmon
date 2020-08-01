package com.tancheon.tmon.dto;

import com.tancheon.tmon.domain.User;
import lombok.*;

import java.util.Calendar;

@Getter
@Setter
public class UserDTO {
    private long id;
    private String email;
    private String password;
    private String name;
    private Calendar signupTime;
    private Calendar lastSigninTime;
    private Calendar passwordChangeTime;
    private boolean emailAuthorized;
    private String emailAuthCode;

    // DTO field
    private String confirmPassword;

    public User toEntity(){
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .signupTime(signupTime)
                .lastSigninTime(lastSigninTime)
                .passwordChangeTime(passwordChangeTime)
                .emailAuthorized(emailAuthorized)
                .emailAuthCode(emailAuthCode)
                .build();
    }
}
