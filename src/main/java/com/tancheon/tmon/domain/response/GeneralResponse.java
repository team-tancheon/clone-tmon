package com.tancheon.tmon.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum GeneralResponse {

    // Common
    SUCCESS(200, "C001", "Success"),
    INTERNAL_SERVER_ERROR(500, "C002", "Internal server error"),

    // User
    DUPLICATE_EMAIL(500, "U001", "Email addresses are duplicate"),
    EMAIL_AUTHENTICATION_FAILED(500, "U002", "Email authentication failed"),
    PASSWORD_MISMATCH(500, "U003", "Password mismatch");

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
