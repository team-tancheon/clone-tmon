package com.tancheon.tmon.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.tancheon.tmon.domain.OAuth;
import com.tancheon.tmon.dto.UserDTO;
import com.tancheon.tmon.exception.EmailAuthFailedException;

public interface UserService {

    boolean signup(UserDTO user);

    boolean authorize(String email, String authCode) throws EmailAuthFailedException;

    JsonNode oauthSignin(OAuth.Provider provider, String code) throws Exception;
}
