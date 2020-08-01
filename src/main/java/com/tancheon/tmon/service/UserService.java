package com.tancheon.tmon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.dto.UserDTO;

public interface UserService {

    boolean signup(UserDTO user);

    boolean authorize(String email, String authCode);

    JsonNode oauthSignin(String provider, String code) throws Exception;
}
