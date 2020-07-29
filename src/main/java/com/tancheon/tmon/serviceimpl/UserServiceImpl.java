package com.tancheon.tmon.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.common.util.UUIDUtil;
import com.tancheon.tmon.domain.User;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.manager.OAuthManager;
import com.tancheon.tmon.repository.UserRepository;
import com.tancheon.tmon.service.OAuthService;
import com.tancheon.tmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    private final OAuthManager oauthManager;

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

        // FIXME : 비밀번호 암호화하기 -> SpringSecurity BCryptPasswordEncoder 이용해서 encode() 필요
        String encryptPassword = user.getPassword();
        user.setPassword(encryptPassword);
        user.setSignUpTime(new GregorianCalendar());

        String randKey = UUIDUtil.createUUID(6);    // 6자리 난수 생성 - 우선 UUID로 사용하였음
        user.setEmailRandKey(randKey);

        User result = userRepository.save(user.toEntity());

        if (userRepository.save(user.toEntity()) != null) {
            sendEmailKey(result.getEmail(), randKey);
            return true;
        }

        return false;
    }

    /**
     * <p>회원 가입시 입력한 이메일로 인증 메일 송신</p>
     * @param email 메일을 보낼 계정
     * @Param emailRandKey 인증키 난수
     */
    @Override
    public void sendEmailKey(String email, String emailRandKey) {

        MimeMessage mail = mailSender.createMimeMessage();

        /**
         * TODO - MailUtil, Thymeleaf Template으로 메일 관련 로직 빼야 함. Key 값 따라서 다른 메일 발송하도록 공통 메시지도 정의 필요
         */

        // FIXME : URL DOMAIN 주의
        String mailText = "<p>Welcome ! </p><br>"
                +"<p>Thank you for creating your Tmon Account</p><br>"
                +"<p>To complete your registration, click the link below</p><br>"
                +"<p><a href='http://localhost:8080/user/complete-signup?email="+email+"&key="+emailRandKey+"'>click link</a></p><br>"
                +"<p>Tmon Team</p><br>";

        try {
            mail.setSubject("Complete your account registration", "utf-8");
            mail.setText(mailText,"utf-8","html");
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
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
