package com.tancheon.tmon.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@AllArgsConstructor
@Controller
public class TestController {

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping(value = "/blog")
    public String blogView() {
        return "blog";
    }

    @GetMapping(value = "/blogdetails")
    public String blogdetails() {
        return "blog-details";
    }

    @GetMapping(value = "/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping(value = "/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/main")
    public String main() {
        return "main";
    }

    @GetMapping(value = "/shopdetails")
    public String shopdetails() {
        return "shop-details";
    }

    @GetMapping(value = "/shopgrid")
    public String shopgrid() {
        return "shop-grid";
    }

    @GetMapping(value = "/shopingcart")
    public String shopingcart() {
        return "shoping-cart";
    }

    @GetMapping(value = "/signin")
    public String signin() {
        return "sign-in";
    }

    @GetMapping(value = "/signup")
    public String signup() {
        return "sign-up";
    }
}
