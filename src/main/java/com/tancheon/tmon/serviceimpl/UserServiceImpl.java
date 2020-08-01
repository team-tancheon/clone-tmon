package com.tancheon.tmon.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.common.util.UUIDUtil;
import com.tancheon.tmon.domain.User;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.manager.OAuthManager;
import com.tancheon.tmon.repository.UserRepository;
import com.tancheon.tmon.service.MailService;
import com.tancheon.tmon.service.OAuthService;
import com.tancheon.tmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OAuthManager oauthManager;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    /**
     * <p>회원가입을 신청한 유저 정보를 저장</p>
     *
     * @param   user        회원가입 입력 정보
     * @return  회원정보DB   입력 성공여부
     */
    @Override
    public boolean signup(UserDTO user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setSignupTime(new GregorianCalendar());    // TODO - yyyy-mm-dd hh:mm:ss 형식으로 들어가도록 수정 필요
        user.setEmailAuthCode(UUIDUtil.createUUID(6));

        if (userRepository.save(user.toEntity()) != null) {
            mailService.sendSignupMessage("Complete your account registration", user.getEmail(), user.getEmailAuthCode());  // subject 공통 메시지화 필요(Complete your account registration)
            return true;
        }

        return false;
    }

    /**
     * <p>이메일 인증을 통한 계정 활성화</p>
     *
     * @param email     인증할 이메일 계정
     * @param authCode  인증코드
     * @return
     */
    @Override
    public boolean authorize(String email, String authCode) {

        User user = userRepository.findByEmailAndEmailAuthCode(email, authCode);

        if (user != null) {
            user.setEmailAuthorized(true);

            if (userRepository.save(user) != null) {
                // TODO - 계정 활성화 메일 발송
                return true;
            }
        }

        return false;
    }

    // TODO - oauthSignup과 통합 고려 -> 사전 확인 필요(권한 동의 절차)
    @Override
    public JsonNode oauthSignin(String provider, String code) throws Exception {

        OAuthService oauthService = oauthManager.getServiceObject(provider);
        String accessToken = oauthService.getAccessToken(code);

        /**
         * TODO - 가져온 정보를 통해 회원 테이블 조회
         * OAuth 회원에 대해서는 이메일 인증 X. 패스워드는 빈 값으로 설정
         */
        JsonNode resultNode = oauthService.getOAuthUserInfo(accessToken);

        return null;
    }
}
