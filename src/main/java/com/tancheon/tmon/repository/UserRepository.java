package com.tancheon.tmon.repository;

import com.tancheon.tmon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmailAndEmailAuthCode(String email, String emailAuthCode);
}
