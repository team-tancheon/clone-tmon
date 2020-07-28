package com.tancheon.tmon.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tancheon.tmon.config.GoogleOauth;
import com.tancheon.tmon.config.KakaoOauth;
import com.tancheon.tmon.domain.User;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.repository.UserRepository;
import com.tancheon.tmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    private final KakaoOauth kakaoOauth;
    private final GoogleOauth googleOauth;

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

    /**
     * 카카오 Oauth) 사용자 토큰 받기
     * @param code 인증 코드 받기 요청으로 얻은 인증 코드
     * @return
     */
    @Override
    public JsonNode getKakaoAccessToken(String code) {
        String appKey = kakaoOauth.getAppkey();
        String redirectURI = "http://localhost:8080/user/kakao-login";
        String requestURI = "https://kauth.kakao.com/oauth/token";

        Map<String, String> info = new HashMap<String, String>();
        info.put("grant_type", "authorization_code");
        info.put("client_id", appKey);
        info.put("redirect_uri", redirectURI);
        info.put("code",code);

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(requestURI);

        final List<NameValuePair> postParams = new ArrayList<>();

        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
        postParams.add(new BasicNameValuePair("client_id", appKey)); // REST API KEY
        postParams.add(new BasicNameValuePair("redirect_uri",redirectURI));
        postParams.add(new BasicNameValuePair("code",code));

        JsonNode returnNode = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(postParams));
            post.setHeader("Content-Type" ,"application/x-www-form-urlencoded;charset=utf-8");
            HttpResponse response = client.execute(post);
            final int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("\nSending 'POST' request to URL : " + requestURI);
            System.out.println("Post parameters : " + postParams);
            System.out.println("Response Code : " + responseCode);

            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();

            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnNode;
    }

    /**
     * 카카오 Oauth) 사용자 토큰 정보 보기
     * @param accessToken  사용자 인증 수단, 액세스 토큰 값
     * @return key { 회원번호([Long]id), 액세스 토큰 만료 시간 ([Integer]expires_in), 토큰이 발급된 앱 ID([Integer]app_id)}
     */
    @Override
    public JsonNode getKakaoInfo(JsonNode accessToken) {

        String requestURI = "https://kapi.kakao.com/v1/user/access_token_info";

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(requestURI);
        get.addHeader("Authorization", "Bearer " + accessToken);

        JsonNode returnNode = null;
        try {
            HttpResponse response = client.execute(get);
            int responseCode = response.getStatusLine().getStatusCode();
            String msg = response.getStatusLine().getReasonPhrase();

            System.out.println("\nSending 'GET' request to URL : " + requestURI);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response Code : " + msg);

            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnNode;
    }

    /**
     * 구글 Oauth) 사용자 토큰 정보 받기
     * @param   code
     * @return
     */
    @Override
    public JsonNode getGoogleAccessToken(String code) {
        String clientId = googleOauth.getClientid();
        String clientSecret =  googleOauth.getClientsecret();
        String redirectURI = "http://localhost:8080/user/google-login";
        String requestURI = "https://oauth2.googleapis.com/token";

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(requestURI);

        final List<NameValuePair> postParams = new ArrayList<>();
        postParams.add(new BasicNameValuePair("code",code));
        postParams.add(new BasicNameValuePair("client_id",clientId));
        postParams.add(new BasicNameValuePair("client_secret",clientSecret));
        postParams.add(new BasicNameValuePair("redirect_uri",redirectURI));
        postParams.add(new BasicNameValuePair("grant_type","authorization_code"));

        JsonNode returnNode = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(postParams));
            post.setHeader("Content-Type" ,"application/x-www-form-urlencoded;charset=utf-8");
            HttpResponse response = client.execute(post);
            final int responseCode = response.getStatusLine().getStatusCode();

            ObjectMapper mapper = new ObjectMapper();

            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnNode;
    }

    @Override
    public JsonNode getGoogleInfo(JsonNode accessToken) {
        String requestURI = "https://www.googleapis.com/oauth2/v2/userinfo";
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpGet get = new HttpGet(requestURI);
        get.addHeader("Authorization", "Bearer " + accessToken);

        JsonNode returnNode = null;
        try {
            HttpResponse response = client.execute(get);
            int responseCode = response.getStatusLine().getStatusCode();
            String msg = response.getStatusLine().getReasonPhrase();

            System.out.println("\nSending 'GET' request to URL : " + requestURI);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response Code : " + msg);

            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnNode;
    }
}
