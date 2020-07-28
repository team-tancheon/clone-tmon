package com.tancheon.tmon.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix="oauth.google")
public class GoogleOauth {
    private String clientid;
    private String clientsecret;
}
