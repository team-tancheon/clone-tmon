package com.tancheon.tmon.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.manager.OAuthManager;
import com.tancheon.tmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/v1")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final OAuthManager oauthManager;

    /**
     * 회원가입
     * @param user 회원가입 입력값 [이메일, 비밀번호, 비밀번호 재입력, 이름]
     */
    @PostMapping(value = "/signup")
    public ResponseEntity<String> signup(UserDTO user){

        /**
         * TODO - 입력값 검증 - @NotNull, @Size 같은 어노테이션으로 DTO 클래스 내에 Validation 어노테이션 적용 후 GlobalExceptionHandler에서 예외 처리 필요
         */

        try {
            userService.signup(user);
        } catch (DataIntegrityViolationException e) {
            String responseMessage = "이미 가입된 이메일입니다.\n다시 입력해주세요.";      // message 공통화 필요
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("signupAccount Failed => " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 회원가입) 이메일 인증 완료
     * @param email 회원 이메일
     * @param authCode 인증 토큰
     */
    @GetMapping(value = "/code/authorize")
    public String authorize(@RequestParam(value = "email") String email,
                            @RequestParam(value = "authCode") String authCode){

        boolean isAuthorized = false;

        try {
            isAuthorized = userService.authorize(email, authCode);
        } catch (Exception e) {
            log.error("authorize failed => " + e.getMessage());
            return "인증 실패";
        }

        return isAuthorized ? "인증 성공" : "인증 실패";
    }

    @GetMapping(value = "/signin/oauth")
    public ResponseEntity<String> oauthSignin(@RequestParam(value = "provider") String provider,
                                              @RequestParam(value = "code") String code) {

        JsonNode result = null;

        try {
            result = userService.oauthSignin(provider, code);
        } catch (Exception e) {
            log.error("oauthSignin Failed => " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }
}
