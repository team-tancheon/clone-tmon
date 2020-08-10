package com.tancheon.tmon.controller;

import com.tancheon.tmon.domain.OAuth;
import com.tancheon.tmon.manager.OAuthManager;
import com.tancheon.tmon.service.OAuthService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@Controller
public class ViewController {

    private final Logger log = LoggerFactory.getLogger(ViewController.class);
    private final OAuthManager oauthManager;

    @GetMapping(value = "/")
    public String getDefaultView() {
        return "index";
    }

    @GetMapping(value = "/view/")
    public String getIndexView() { return "index"; }

    @GetMapping(value = "/view/blog")
    public String getBlogViewView() {
        return "blog";
    }

    @GetMapping(value = "/view/blogdetails")
    public String getBlogdetailsView() {
        return "blog-details";
    }

    @GetMapping(value = "/view/checkout")
    public String getCheckoutView() {
        return "checkout";
    }

    @GetMapping(value = "/view/contact")
    public String getContactView() {
        return "contact";
    }

    @GetMapping(value = "/view/main")
    public String getMainView() {
        return "main";
    }

    @GetMapping(value = "/view/shopdetails")
    public String getShopdetailsView() {
        return "shop-details";
    }

    @GetMapping(value = "/view/shopgrid")
    public String getShopgridView() {
        return "shop-grid";
    }

    @GetMapping(value = "/view/shopingcart")
    public String getShopingcartView() {
        return "shoping-cart";
    }

    @GetMapping(value = "/view/signin")
    public String getSigninView() {
        return "sign-in";
    }

    @GetMapping(value = "/view/signup")
    public String getSignupView() {
        return "sign-up";
    }

    @GetMapping(value = "/view/signin/oauth/{provider}")
    public void getOAuthSigninView(@NotNull @PathVariable(value = "provider") OAuth.Provider provider,
                                   HttpServletResponse response) {

        try {
            OAuthService oauthService = oauthManager.getServiceObject(provider.name());
            response.sendRedirect(oauthService.getRedirectUrl());
        } catch (Exception e) {
            log.error("getOAuthSigninView Failed => " + e.getMessage());
        }
    }
}
