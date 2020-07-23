package com.tancheon.tmon.repository;

import com.tancheon.tmon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByEmailAndEmailRandKey(String email, String key);

//    O findByEmail(String email);
}
