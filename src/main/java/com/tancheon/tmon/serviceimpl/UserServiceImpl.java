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
    public boolean registerAccount(UserDTO user) {

        /**
         * TODO - 회원가입 정보 디비 입력 & 비밀번호 암호화 & 이메일 인증 보내기
         */
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setSignUpTime(new GregorianCalendar());
        user.setEmailRandKey(UUIDUtil.createUUID(6));

        if (userRepository.save(user.toEntity()) != null) {
            mailService.sendSignupMessage(null, user.getEmail(), user.getEmailRandKey());
            return true;
        }

        return false;
    }

    /**
     * <p>이메일 인증을 통한 계정 활성화</p>
     *
     * @param email 인증할 이메일 계정
     * @param code 인증코드
     * @return
     */
    @Override
    public boolean signUpComplete(String email, String code) {

        User user = userRepository.findByEmailAndEmailRandKey(email, code);

        if (code.equals(user.getEmailRandKey())) {
            user.setEmailAuthorized(true);
            return (userRepository.save(user) != null);
        }

        // TODO - 계정 활성화 메일 발송

        return false;
    }

    @Override
    public JsonNode getOAuthUserInfo(String provider, String code) throws Exception {

        OAuthService oauthService = oauthManager.getServiceObject(provider);
        String accessToken = oauthService.getAccessToken(code);

        return oauthService.getOAuthUserInfo(accessToken);
    }
}
