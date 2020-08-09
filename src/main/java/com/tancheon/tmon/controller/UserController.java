package com.tancheon.tmon.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.domain.OAuth;
import com.tancheon.tmon.domain.response.GeneralResponse;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.exception.EmailAuthFailedException;
import com.tancheon.tmon.exception.NotAuthorizedException;
import com.tancheon.tmon.exception.PasswordMismatchException;
import com.tancheon.tmon.exception.UserNotFoundException;
import com.tancheon.tmon.manager.OAuthManager;
import com.tancheon.tmon.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user/v1")
@Validated
public class UserController extends BaseController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final OAuthManager oauthManager;

    @ApiOperation(value = "로그인", httpMethod = "POST", notes = "로그인 API")
    @PostMapping(value = "/signin")
    public ResponseEntity signin(@ApiParam(value = "email", required = true)
                                 @RequestParam(value = "email") String email,
                                 @ApiParam(value = "password", required = true)
                                 @RequestParam(value = "password") String password) {

        try {
            userService.signin(email, password);
        } catch (UserNotFoundException | PasswordMismatchException e) {
            responseError(GeneralResponse.SIGNIN_FAILED);
        } catch (NotAuthorizedException e) {
            responseError(GeneralResponse.NOT_EMAIL_AUTHORIZED);
        } catch (Exception e) {
            log.error("signin Failed => " + e.getMessage());
            responseError(GeneralResponse.INTERNAL_SERVER_ERROR);
        }

        return responseSuccess();
    }

    @ApiOperation(value = "회원가입", httpMethod = "POST", notes = "회원가입 API")
    @PostMapping(value = "/signup")
    public ResponseEntity signup(@Valid UserDTO user){

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return responseError(GeneralResponse.PASSWORD_MISMATCH);
        }

        try {
            userService.signup(user);
        } catch (DataIntegrityViolationException e) {
            return responseError(GeneralResponse.REGISTERED_USER);
        } catch (Exception e) {
            log.error("signupAccount Failed => " + e.getMessage());
            return responseError(GeneralResponse.INTERNAL_SERVER_ERROR);
        }

        return responseSuccess(GeneralResponse.SIGNUP_SUCCESS);
    }

    @ApiOperation(value = "이메일 인증", httpMethod = "GET", notes = "이메일 인증 API")
    @GetMapping(value = "/code/authorize")
    public ResponseEntity authorize(@ApiParam(value = "이메일", required = true)
                                    @NotEmpty @Email @RequestParam(value = "email") String email,
                                    @ApiParam(value = "이메일 인증 코드", required = true)
                                    @NotEmpty @RequestParam(value = "authCode") String authCode) {

        boolean isAuthorized = false;

        try {
            isAuthorized = userService.authorize(email, authCode);
        } catch (EmailAuthFailedException e) {
            log.error("authorize failed => " + e.getMessage());
            return responseError(GeneralResponse.EMAIL_AUTHENTICATION_FAILED);
        } catch (Exception e) {
            log.error("authorize failed => " + e.getMessage());
            return responseError(GeneralResponse.INTERNAL_SERVER_ERROR);
        }

        return responseSuccess();
    }

    @ApiOperation(value = "OAuth 로그인", httpMethod = "GET", notes = "OAuth 로그인 API")
    @GetMapping(value = "/signin/oauth/{provider}")
    public ResponseEntity oauthSignin(@ApiParam(value = "OAuth Provider(kakao, google)", required = true)
                                      @NotNull @PathVariable(value = "provider") OAuth.Provider provider,
                                      @ApiParam(value = "사용자 access_token 받기 요청에 필요한 코드", required = true)
                                      @NotEmpty @RequestParam(value = "code") String code) {

        JsonNode result = null;

        try {
            result = userService.oauthSignin(provider, code);
        } catch (Exception e) {
            log.error("oauthSignin Failed => " + e.getMessage());
            return responseError(GeneralResponse.INTERNAL_SERVER_ERROR);
        }

        return responseSuccess();
    }
}
