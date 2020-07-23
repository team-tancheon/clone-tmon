package com.tancheon.tmon.dto;

import com.tancheon.tmon.domain.User;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

@Data
//@NoArgsConstructor
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
    private String rePassword;


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
