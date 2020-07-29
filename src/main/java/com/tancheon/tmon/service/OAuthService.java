package com.tancheon.tmon.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface OAuthService {

    String getAccessToken(String code);

    JsonNode getOAuthUserInfo(String accessToken);

    String getRedirectUrl();
}
