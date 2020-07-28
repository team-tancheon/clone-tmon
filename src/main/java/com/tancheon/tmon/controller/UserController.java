package com.tancheon.tmon.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.config.GoogleOauth;
import com.tancheon.tmon.config.KakaoOauth;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    // 인증 토큰
    private final GoogleOauth googleOauth;
    private final KakaoOauth kakaoOauth;

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
        boolean isAuthorized = false;
        try {
            isAuthorized = userService.signUpComplete(email, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAuthorized?"인증 성공":"인증 실패";
    }

    /**
     * Kakao Oauth) 카카오 로그인창으로 리다이렉션
     */
    @RequestMapping(value="/kakao-oauth", method = RequestMethod.GET)
    public void kakaoLoginView(HttpServletResponse response) throws IOException {
        String appKey = kakaoOauth.getAppkey();
        String redirectURI = "http://localhost:8080/user/kakao-login";

        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + appKey);
        url.append("&redirect_uri="+redirectURI);
        url.append("&response_type=code");

        response.sendRedirect(url.toString());
    }

    /**
     * Kakao Oauth) 카카오 계정 인증
     *
     * @param code
     * @param ra
     * @param session
     * @param response
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/kakao-login",produces = "application/json", method = {RequestMethod.GET,RequestMethod.POST})
    public String kakaoLogin(@RequestParam("code")String code, RedirectAttributes ra, HttpSession session, HttpServletResponse response) throws IOException {
        JsonNode accessToken = userService.getKakaoAccessToken(code);
        JsonNode userInfo = userService.getKakaoInfo(accessToken.get("access_token"));

        String userId = userInfo.get("id").asText();
        System.out.println("내 아이디 출력 : "+userId);
        return "내 아이디 임시 출력 : "+userId;
    }

    /**
     * google Oauth) 구글 로그인창으로 리다이렉션
     */
    @RequestMapping(value="/google-oauth", method = RequestMethod.GET)
    public void googleLoginView(HttpServletResponse response) throws IOException {
        String cliendId = googleOauth.getClientid();
        String redirectURI = "http://localhost:8080/user/google-login";
        String accessType = "offline";
        String includeGrantedScopes = "true";

        StringBuffer url = new StringBuffer();
        url.append("https://accounts.google.com/o/oauth2/v2/auth?");
        url.append("scope=email");
        url.append("&access_type=" + accessType);
        url.append("&include_granted_scopes="+includeGrantedScopes);
        url.append("&state=state_parameter_passthrough_value");
        url.append("&redirect_uri="+redirectURI);
        url.append("&client_id="+cliendId);
        url.append("&response_type=code");

        response.sendRedirect(url.toString());
    }

    @GetMapping("/google-login")
    public String googleLogin(@RequestParam("code")String code){
        JsonNode accessToken = userService.getGoogleAccessToken(code);
        JsonNode userInfo = userService.getGoogleInfo(accessToken.get("access_token"));

        return userInfo.toString();
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
