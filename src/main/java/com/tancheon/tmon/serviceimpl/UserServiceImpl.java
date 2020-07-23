package com.tancheon.tmon.serviceimpl;

import com.tancheon.tmon.domain.User;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.repository.UserRepository;
import com.tancheon.tmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.GregorianCalendar;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    /**
     * 회원가입을 신청한 유저 정보를 저장
     *
     * @param user 회원가입 입력 정보
     * @return 회원정보 DB 입력 성공여부
     */
    @Override
    public boolean registerAccount(UserDTO user) {
        // FIXME : 비밀번호 암호화하기
        String encryptPwd = user.getPassword(); //
        user.setPassword(encryptPwd);

        user.setSignUpTime(new GregorianCalendar());
        // FIXME : 난수 처리
        String emailRandKey = "emailkey_temp";
        user.setEmailRandKey(emailRandKey);

        User save = userRepository.save(user.toEntity());

        if(save != null){
            sendEmailKey(save.getEmail(), emailRandKey);
            save.setEmailRandKey(emailRandKey);
            return true;
        }
        return false;
    }

    /**
     * 회원 가입시 입력한 이메일로 인증 메일 송신
     * @param email 메일을 보낼 계정
     * @Param emailRandKey 인증키 난수
     */
    @Override
    public void sendEmailKey(String email, String emailRandKey) {

        MimeMessage mail = mailSender.createMimeMessage();
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
     * 이메일 인증을 통한 계정 활성화
     * @param email 인증할 이메일 계정
     * @param key 인증키
     * @return
     */
    @Override
    public boolean signUpComplete(String email, String key) {
        User user = userRepository.findByEmailAndEmailRandKey(email,key);

        user.setEmailAuthorized(true);

        if(userRepository.save(user).isEmailAuthorized()){
            return true;
        }
        return false;
    }

}
