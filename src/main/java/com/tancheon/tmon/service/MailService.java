package com.tancheon.tmon.service;

import lombok.Getter;

public interface MailService {

    @Getter
    enum MailTemplate {
        AUTHORIZE_MAIL("Complete your account registration", "mail/authorize-mail"),
        CREATE_ACCOUNT_SUCCESS_MAIL("Congratulations on signing up for Clone-TMON", "mail/create-account-success-mail");

        private String subject;
        private String filePath;

        MailTemplate(String subject, String filePath) {
            this.subject = subject;
            this.filePath = filePath;
        }
    }

    void sendSignupMail(String toEmail, String authCode);

    void sendCreateAccountSuccessMail(String toEmail);
}
