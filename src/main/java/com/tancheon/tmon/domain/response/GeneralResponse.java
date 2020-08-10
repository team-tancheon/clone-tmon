package com.tancheon.tmon.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum GeneralResponse {

    // Common
    SUCCESS(200, "C001", "Success"),
    SIGNUP_SUCCESS(200, "C002", "인증 메일이 발송되었습니다."),
    INTERNAL_SERVER_ERROR(500, "C003", "서버에 문제가 발생하였습니다."),

    // User
    REGISTERED_USER(400, "U001", "등록된 이메일입니다."),
    EMAIL_AUTHENTICATION_FAILED(401, "U002", "이메일 인증에 실패하였습니다."),
    SIGNIN_FAILED(401, "U003", "아이디 또는 비밀번호가 일치하지 않습니다."),
    PASSWORD_MISMATCH(401, "U004", "비밀번호가 일치하지 않습니다."),
    NOT_EMAIL_AUTHORIZED(401, "U005", "이메일 인증을 해주세요.");


    private int status;
    private String code;
    private String message;

    @Setter
    private String cause;

    GeneralResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
