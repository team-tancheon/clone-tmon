package com.tancheon.tmon.dto;

import com.tancheon.tmon.domain.User;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.*;
import java.time.ZonedDateTime;

@Getter
@Setter
public class UserDTO {

    private long id;

    @ApiParam(value = "이메일", required = true)
    @NotNull
    @Size(min = 1, max = 255)
    @Email
    private String email;

    @ApiParam(value = "이름", required = true)
    @Pattern(regexp = "^[가-힣]{2,30}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$")
    private String name;

    @ApiParam(value = "비밀번호", required = true)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[!@#$%^&*()])(?=.*[\\d])[A-Za-z\\d!@#$%^&*()]{8,20}$")
    private String password;

    @ApiParam(value = "비밀번호 확인", required = true)
    @NotEmpty
    private String confirmPassword;

    private ZonedDateTime signupTime;
    private ZonedDateTime lastSigninTime;
    private ZonedDateTime passwordChangeTime;
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
