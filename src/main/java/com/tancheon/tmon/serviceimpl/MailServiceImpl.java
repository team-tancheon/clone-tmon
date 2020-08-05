package com.tancheon.tmon.serviceimpl;

import com.tancheon.tmon.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendSignupMail(String toEmail, String authCode) {
        Context context = new Context();
        context.setVariable("email", toEmail);
        context.setVariable("authCode", authCode);

        sendMail(MailTemplate.AUTHORIZE_MAIL, context);
    }

    @Override
    public void sendCreateAccountSuccessMail(String toEmail) {
        Context context = new Context();
        context.setVariable("email", toEmail);

        sendMail(MailTemplate.CREATE_ACCOUNT_SUCCESS_MAIL, context);
    }

    private void sendMail(MailTemplate template, Context context) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            String htmlContent = templateEngine.process(template.getFilePath(), context);

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(template.getSubject());
            helper.setTo(new InternetAddress(context.getVariable("email").toString()));
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
