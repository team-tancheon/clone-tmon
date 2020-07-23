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

    /**
     * 회원가입)
     * @param user 회원가입 입력값 [이메일, 비밀번호, 비밀번호 재입력, 이름]
     */
    @PostMapping("signup")
    public String signUpAccount(@RequestBody UserDTO user){
        System.out.println("회원가입");
        // TODO 입력값 검사
        
        try {
            // TODO 회원가입 정보 디비 입력 & 비밀번호 암호화 & 이메일 인증 보내기
            userService.registerAccount(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "signup 완료";
    }

    /**
     * 회원가입) 이메일 인증 완료
     * @param email 회원 이메일
     * @param key 인증키
     */
    @GetMapping("complete-signup")
    public String signUpComplete(@RequestParam String email, String key){
        System.out.println("email : "+email+" / key : "+key);
        boolean isAuthorized = false;
        try {
            isAuthorized = userService.signUpComplete(email, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAuthorized?"인증 성공":"인증 실패";
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
