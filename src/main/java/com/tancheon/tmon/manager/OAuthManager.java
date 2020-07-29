package com.tancheon.tmon.manager;

import com.tancheon.tmon.properties.OAuthProperties;
import com.tancheon.tmon.service.OAuthService;
import com.tancheon.tmon.serviceimpl.GoogleServiceImpl;
import com.tancheon.tmon.serviceimpl.KakaoServiceImpl;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Service
public class OAuthManager {

    private Map<String, Class<? extends OAuthService>> oauthServiceMap;

    private OAuthProperties properties;

    public OAuthManager(OAuthProperties properties) {
        this.properties = properties;
        oauthServiceMap = new HashMap<>();
        oauthServiceMap.put(GoogleServiceImpl.provider, GoogleServiceImpl.class);
        oauthServiceMap.put(KakaoServiceImpl.provider, KakaoServiceImpl.class);
    }

    public OAuthService getServiceObject(String provider) throws Exception {

        Class<? extends OAuthService> serviceClass = oauthServiceMap.get(provider);

        if (serviceClass == null) {
            throw new Exception();
        }

        OAuthService service = null;

        try {
            service = serviceClass.getConstructor(OAuthProperties.class).newInstance(this.properties);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return service;
    }
}
