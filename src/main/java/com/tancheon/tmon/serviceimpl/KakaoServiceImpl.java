package com.tancheon.tmon.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tancheon.tmon.properties.OAuthProperties;
import com.tancheon.tmon.service.OAuthService;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class KakaoServiceImpl implements OAuthService {

    private final OAuthProperties properties;

    public static final String provider = "kakao";


    /**
     * <p>카카오 Oauth) 사용자 토큰 받기</p>
     *
     * @param   code    인증 코드 받기 요청으로 얻은 인증 코드
     * @return
     */
    @Override
    public String getAccessToken(String code) {

        String clientId = properties.getKakao().get("client-id");
        String redirectUri = "http://localhost:8080/user/v1/signin/oauth/kakao";
        String requestUri = "https://kauth.kakao.com/oauth/token";

        final List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("client_id", clientId)); // REST API KEY
        params.add(new BasicNameValuePair("redirect_uri", redirectUri));
        params.add(new BasicNameValuePair("code",code));

        JsonNode returnNode = null;

        try {
            HttpPost request = new HttpPost(requestUri);
            request.setEntity(new UrlEncodedFormEntity(params));
            request.setHeader("Content-Type" ,"application/x-www-form-urlencoded;charset=utf-8");

            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("Sending 'POST' request to URL : " + requestUri);
            System.out.println("Post parameters : " + params);
            System.out.println("Response Code : " + responseCode);

            // JSON 형태 반환값 처리
            ObjectMapper mapper = new ObjectMapper();
            returnNode = new ObjectMapper().readTree(response.getEntity().getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnNode.get("access_token").textValue();
    }

    /**
     * <p>카카오 Oauth) 사용자 토큰 정보 보기</p>
     *
     * @param   accessToken 사용자 인증 수단, 액세스 토큰 값
     * @return  key         { 회원번호([Long]id), 액세스 토큰 만료 시간 ([Integer]expires_in), 토큰이 발급된 앱 ID([Integer]app_id)}
     */
    @Override
    public JsonNode getOAuthUserInfo(String accessToken) {

        String requestURI = properties.getKakao().get("api-url") + "/user/access_token_info";

        JsonNode returnNode = null;

        try {
            HttpGet request = new HttpGet(requestURI);
            request.addHeader("Authorization", "Bearer " + URLEncoder.encode(accessToken, "UTF-8"));

            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            int responseCode = response.getStatusLine().getStatusCode();
            String msg = response.getStatusLine().getReasonPhrase();

            System.out.println("Sending 'GET' request to URL : " + requestURI);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response Message : " + msg);

            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnNode;
    }

    @Override
    public String getRedirectUrl() {

        String clientId = properties.getKakao().get("client-id");
        String redirectURI = "http://localhost:8080/user/v1/signin/oauth/kakao";

        StringBuffer url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + clientId);
        url.append("&redirect_uri="+redirectURI);
        url.append("&response_type=code");

        return url.toString();
    }
}
