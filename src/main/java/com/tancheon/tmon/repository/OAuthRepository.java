package com.tancheon.tmon.repository;

import com.tancheon.tmon.domain.OAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthRepository extends JpaRepository<OAuth, Long>{

}
