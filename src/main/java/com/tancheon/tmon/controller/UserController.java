package com.tancheon.tmon.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.manager.OAuthManager;
import com.tancheon.tmon.service.OAuthService;
import com.tancheon.tmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final OAuthManager oauthManager;

    /**
     * 회원가입)
     * @param user 회원가입 입력값 [이메일, 비밀번호, 비밀번호 재입력, 이름]
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signupAccount(UserDTO user){

        /**
         * TODO - 입력값 검증 - @NotNull, @Size 같은 어노테이션으로 DTO 클래스 내에 Validation 어노테이션 적용 후 GlobalExceptionHandler에서 예외 처리 필요
         */

        try {
            userService.registerAccount(user);
        } catch (Exception e) {
            log.error("signupAccount Failed => " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 회원가입) 이메일 인증 완료
     * @param email 회원 이메일
     * @param code 인증코드
     */
    @GetMapping("/authorize")
    public String authorize(@RequestParam(value = "email") String email,
                            @RequestParam(value = "code") String code){

        boolean isAuthorized = false;

        try {
            isAuthorized = userService.signUpComplete(email, code);
        } catch (Exception e) {
            log.error("authorize failed => " + e.getMessage());
            return "인증 실패";
        }

        return isAuthorized ? "인증 성공" : "인증 실패";
    }

    @GetMapping(value="/oauth/loginview")
    public void oauthLoginView(@RequestParam(value = "provider") String provider,
                               HttpServletResponse response) {

        try {
            OAuthService oauthService = oauthManager.getServiceObject(provider);
            response.sendRedirect(oauthService.getRedirectUrl());
        } catch (Exception e) {
            log.error("oauthLoginView Failed => " + e.getMessage());
        }
    }

    // TODO - 논의 후 redirect uri 변경 필요
    @GetMapping(value = "/{provider}-login")
    public ResponseEntity<String> getOAuthUserInfo(@PathVariable(value = "provider") String provider,
                                                   @RequestParam(value = "code") String code) {

        JsonNode result = null;

        try {
            result = userService.getOAuthUserInfo(provider, code);
        } catch (Exception e) {
            log.error("getOAuthUserInfo Failed => " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }
}
