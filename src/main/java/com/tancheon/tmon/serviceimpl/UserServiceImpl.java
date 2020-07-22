package com.tancheon.tmon.serviceimpl;

import com.tancheon.tmon.domain.User;
import com.tancheon.tmon.dto.DomDTO;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.repository.DomRepository;
import com.tancheon.tmon.repository.UserRepository;
import com.tancheon.tmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DomRepository domRepository;

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

        // TODO : 유저 정보 저장
        user.setSignUpTime(new GregorianCalendar());

        if(userRepository.save(user.toEntity()) != null){
            return true;
        }
        return false;
    }

    /**
     * 회원 가입시 입력한 이메일로 인증 메일 송신
     * @param email
     */
    @Override
    public void sendEmailKey(String email) {
        // FIXME : 난수 키 생성
        String key = "emailKey";
        
        MimeMessage mail = mailSender.createMimeMessage();
        // FIXME : URL DOMAIN 주의
        String mailText = "<p>Welcome ! </p><br>"
                +"<p>Thank you for creating your Tmon Account</p><br>"
                +"<p>To complete your registration, click the link below</p><br>"
                +"<p><a href='http://localhost:8080/user/complete-signup?email="+email+"&emailkey="+key+"'></p><br>"
                +"<p>Tmon Team</p><br>";
        try {

            mail.setSubject("Complete your account registration", "utf-8");
            mail.setText(mailText,"utf-8","html");
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mailSender.send(mail);

            // TODO : USER에 난수 키 컬럼 생성 후 저장
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void testCode(DomDTO dom) {
        System.out.println("서비스 들어왔다");
        System.out.println(dom.toEntity().toString());
        domRepository.save(dom.toEntity());
    }

}
