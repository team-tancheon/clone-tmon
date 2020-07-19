package com.tancheon.tmon.controller;

import com.tancheon.tmon.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
//    private final UserService userService;

    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public ResponseEntity test() {

        try {

        } catch (Exception e) {
            log.error("test failed => " + e.getMessage());
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}
