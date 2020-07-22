package com.tancheon.tmon.controller;

import com.tancheon.tmon.domain.User;
import com.tancheon.tmon.dto.DomDTO;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("signup")
    public String signUpAccount(@RequestBody UserDTO user){
        System.out.println("회원가입");
        // TODO 입력값 검사
        
        // TODO 회원가입 정보 디비 입력 & 비밀번호 암호화
        if(userService.registerAccount(user)){
        // TODO 이메일 인증 보내기
            userService.sendEmailKey(user.getEmail());

        }

        return "";
    }

    @GetMapping("complete-signup")
    public String signUpComplete(){

        return "";
    }
    @PostMapping("/test")
    public ResponseEntity test(@RequestBody DomDTO dom){
        System.out.println("테스트 코드");
        userService.testCode(dom);

        return null;
    }
//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String indexView() {
//        return "index";
//    }
//
//    @RequestMapping(value = "/api", method = RequestMethod.GET)
//    public ResponseEntity test() {
//
//        try {
//
//        } catch (Exception e) {
//            log.error("test failed => " + e.getMessage());
//        }
//
//        return new ResponseEntity(HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/signin", method = RequestMethod.GET)
//    public String signInView() {
//        return "sign-in";
//    }
}
