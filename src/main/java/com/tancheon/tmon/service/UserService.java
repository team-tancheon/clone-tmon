package com.tancheon.tmon.service;

import com.tancheon.tmon.domain.User;
import com.tancheon.tmon.dto.DomDTO;
import com.tancheon.tmon.dto.UserDTO;

public interface UserService {

//    boolean registerUser(UserDTO user);
    boolean registerAccount(UserDTO user);

    void sendEmailKey(String email, String emailRandKey);

//    void testCode(DomDTO dom);

    boolean signUpComplete(String email, String key);
}
