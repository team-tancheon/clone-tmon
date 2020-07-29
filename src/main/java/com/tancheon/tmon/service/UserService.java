package com.tancheon.tmon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.dto.UserDTO;

public interface UserService {

    boolean registerAccount(UserDTO user);

    void sendEmailKey(String email, String emailRandKey);

    boolean signUpComplete(String email, String key);

    JsonNode getOAuthUserInfo(String provider, String code) throws Exception;
}
