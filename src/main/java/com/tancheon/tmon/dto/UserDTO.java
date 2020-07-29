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
    private Calendar signUpTime;
    private Calendar lastLoginTime;
    private Calendar passwordChangeTime;
    private boolean emailAuthorized;
    private String emailRandKey;

    // DTO field
    private String passwordConfirm;

    public User toEntity(){
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .signUpTime(signUpTime)
                .lastLoginTime(lastLoginTime)
                .passwordChangeTime(passwordChangeTime)
                .emailAuthorized(emailAuthorized)
                .emailRandKey(emailRandKey)
                .build();
    }
}
