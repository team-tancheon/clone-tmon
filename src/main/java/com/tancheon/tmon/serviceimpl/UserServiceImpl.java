package com.tancheon.tmon.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.common.util.UUIDUtil;
import com.tancheon.tmon.domain.OAuth;
import com.tancheon.tmon.domain.User;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.exception.EmailAuthFailedException;
import com.tancheon.tmon.exception.NotAuthorizedException;
import com.tancheon.tmon.exception.PasswordMismatchException;
import com.tancheon.tmon.exception.UserNotFoundException;
import com.tancheon.tmon.manager.OAuthManager;
import com.tancheon.tmon.repository.UserRepository;
import com.tancheon.tmon.service.MailService;
import com.tancheon.tmon.service.OAuthService;
import com.tancheon.tmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Service
public
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OAuthManager oauthManager;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signin(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException();
        } else if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordMismatchException();
        } else if (!user.isEmailAuthorized()) {
            throw new NotAuthorizedException();
        }

        user.setLastSigninTime(ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
        userRepository.save(user);
    }

    /**
     * <p>회원가입을 신청한 유저 정보를 저장</p>
     *
     * @param   user        회원가입 입력 정보
     * @return  회원정보DB   입력 성공여부
     */
    @Override
    public void signup(UserDTO user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setSignupTime(ZonedDateTime.now(ZoneId.of("Asia/Seoul")));
        user.setEmailAuthCode(UUIDUtil.createUUID(6));

        if (userRepository.save(user.toEntity()) != null) {
            mailService.sendSignupMail(user.getEmail(), user.getEmailAuthCode());
        }
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
                mailService.sendCreateAccountSuccessMail(user.getEmail());
                return true;
            }
        }

        throw new EmailAuthFailedException();
    }

    // TODO - oauthSignup과 통합 고려 -> 사전 확인 필요(권한 동의 절차)
    @Override
    public JsonNode oauthSignin(OAuth.Provider provider, String code) throws Exception {

        OAuthService oauthService = oauthManager.getServiceObject(provider.name());
        String accessToken = oauthService.getAccessToken(code);

        /**
         * TODO - 가져온 정보를 통해 회원 테이블 조회
         * OAuth 회원에 대해서는 이메일 인증 X. 패스워드는 빈 값으로 설정
         */
        JsonNode resultNode = oauthService.getOAuthUserInfo(accessToken);

        return null;
    }
}
