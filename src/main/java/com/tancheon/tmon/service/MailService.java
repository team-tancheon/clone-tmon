package com.tancheon.tmon.service;

public interface MailService {

    boolean sendSignupMessage(String subject, String toEmail, String authCode);


}
