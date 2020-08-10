package com.tancheon.tmon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.domain.OAuth;
import com.tancheon.tmon.dto.UserDTO;

public interface UserService {

    void signin(String email, String password);

    void signup(UserDTO user);

    void authorize(String email, String authCode);

    JsonNode oauthSignin(OAuth.Provider provider, String code) throws Exception;
}
