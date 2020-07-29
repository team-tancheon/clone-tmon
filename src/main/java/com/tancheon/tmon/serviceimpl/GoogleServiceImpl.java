package com.tancheon.tmon.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tancheon.tmon.properties.OAuthProperties;
import com.tancheon.tmon.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GoogleServiceImpl implements OAuthService {

    private final OAuthProperties properties;

    public static final String provider = "google";


    /**
     * <p>구글 Oauth) 사용자 토큰 정보 받기</p>
     *
     * @param   code
     * @return
     */
    @Override
    public String getAccessToken(String code) {

        String clientId = properties.getGoogle().get("client-id");
        String clientSecret = properties.getGoogle().get("client-secret");
        String redirectURI = "http://localhost:8080/user/google-login";
        String requestURI = "https://oauth2.googleapis.com/token";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("client_id", clientId));
        params.add(new BasicNameValuePair("client_secret", clientSecret));
        params.add(new BasicNameValuePair("redirect_uri", redirectURI));
        params.add(new BasicNameValuePair("grant_type","authorization_code"));

        JsonNode returnNode = null;

        try {
            HttpPost request = new HttpPost(requestURI);
            request.setEntity(new UrlEncodedFormEntity(params));
            request.setHeader("Content-Type" , ContentType.APPLICATION_FORM_URLENCODED.toString());

            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            int responseCode = response.getStatusLine().getStatusCode();

            ObjectMapper mapper = new ObjectMapper();
            returnNode = mapper.readTree(response.getEntity().getContent());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnNode.get("access_token").textValue();
    }

    @Override
    public JsonNode getOAuthUserInfo(String accessToken) {

        String requestURI = properties.getGoogle().get("api-url") + "/userinfo";

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

        String clientId = properties.getGoogle().get("client-id");
        String redirectUri = "http://localhost:8080/user/google-login";
        String accessType = "offline";
        String includeGrantedScopes = "true";

        StringBuffer url = new StringBuffer();
        url.append("https://accounts.google.com/o/oauth2/v2/auth?");
        url.append("scope=email");
        url.append("&access_type=" + accessType);
        url.append("&include_granted_scopes=" + includeGrantedScopes);
        url.append("&state=state_parameter_passthrough_value");
        url.append("&redirect_uri=" + redirectUri);
        url.append("&client_id=" + clientId);
        url.append("&response_type=code");

        return url.toString();
    }
}
