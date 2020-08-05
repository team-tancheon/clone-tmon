package com.tancheon.tmon.controller;

import com.tancheon.tmon.domain.response.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

abstract class BaseController {

    ResponseEntity responseSuccess() {
        return new ResponseEntity<>(GeneralResponse.SUCCESS, HttpStatus.valueOf(GeneralResponse.SUCCESS.getStatus()));
    }

    ResponseEntity responseError(GeneralResponse response) {
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatus()));
    }
}
