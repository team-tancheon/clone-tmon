package com.tancheon.tmon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.domain.User;
import com.tancheon.tmon.dto.DomDTO;
import com.tancheon.tmon.dto.UserDTO;

public interface UserService {

    boolean registerAccount(UserDTO user);

    void sendEmailKey(String email, String emailRandKey);

    boolean signUpComplete(String email, String key);

    JsonNode getKakaoAccessToken(String code);

    JsonNode getKakaoInfo(JsonNode access_token);

    JsonNode getGoogleAccessToken(String code);

    JsonNode getGoogleInfo(JsonNode access_token);
}
