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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;


@AllArgsConstructor
@Controller
@RequestMapping(value = "/view")
public class ViewController {

    private final Logger log = LoggerFactory.getLogger(ViewController.class);
    private final OAuthManager oauthManager;

    @GetMapping(value = "/")
    public String getIndexView() { return "index"; }

    @GetMapping(value = "/blog")
    public String getBlogViewView() {
        return "blog";
    }

    @GetMapping(value = "/blogdetails")
    public String getBlogdetailsView() {
        return "blog-details";
    }

    @GetMapping(value = "/checkout")
    public String getCheckoutView() {
        return "checkout";
    }

    @GetMapping(value = "/contact")
    public String getContactView() {
        return "contact";
    }

    @GetMapping(value = "/main")
    public String getMainView() {
        return "main";
    }

    @GetMapping(value = "/shopdetails")
    public String getShopdetailsView() {
        return "shop-details";
    }

    @GetMapping(value = "/shopgrid")
    public String getShopgridView() {
        return "shop-grid";
    }

    @GetMapping(value = "/shopingcart")
    public String getShopingcartView() {
        return "shoping-cart";
    }

    @GetMapping(value = "/signin")
    public String getSigninView() {
        return "sign-in";
    }

    @GetMapping(value = "/signup")
    public String getSignupView() {
        return "sign-up";
    }

    @GetMapping(value = "/signin/oauth/{provider}")
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
