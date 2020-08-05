package com.tancheon.tmon.dto;

import com.tancheon.tmon.domain.User;
import lombok.*;

import javax.validation.constraints.*;
import java.util.Calendar;

@Getter
@Setter
public class UserDTO {

    private long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Email
    private String email;

    @Pattern(regexp = "^[가-힣]{2,30}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$")
    private String name;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[!@#$%^&*()])(?=.*[\\d])[A-Za-z\\d!@#$%^&*()]{8,20}$")
    private String password;

    @NotEmpty
    private String confirmPassword;

    private Calendar signupTime;
    private Calendar lastSigninTime;
    private Calendar passwordChangeTime;
    private boolean emailAuthorized;
    private String emailAuthCode;

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
