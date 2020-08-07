package com.tancheon.tmon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Builder
@ToString
@Entity(name = "oauth")
public class OAuth {

    public enum Provider {
        google,
        kakao
    }

    @Id
    @Column(name = "oauth_id", length = 255)
    private String oauthId;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "provider", nullable = false, columnDefinition = "varchar(10)")
    private Provider provider;
}
