package com.tancheon.tmon.controller;

import com.tancheon.tmon.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@AllArgsConstructor
@Controller
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
//    private final UserService userService;


//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String indexView() {
//        return "index";
//    }
//
//    @RequestMapping(value = "/api", method = RequestMethod.GET)
//    public ResponseEntity test() {
//
//        try {
//
//        } catch (Exception e) {
//            log.error("test failed => " + e.getMessage());
//        }
//
//        return new ResponseEntity(HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/signin", method = RequestMethod.GET)
//    public String signInView() {
//        return "sign-in";
//    }
}
