package dev.saha.otpauthserverweb.service.impl;



import dev.saha.otpauthserverweb.exceptions.EmailException;
import dev.saha.otpauthserverweb.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

import static dev.saha.otpauthserverweb.util.MessageTemplate.welcomeMessage;
import static dev.saha.otpauthserverweb.util.MessageTemplate.welcomeMessageSubject;


@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String emailSender;

    @Value("${spring.mail.password}")
    private String emailPassword;

    private Properties properties;

    private void init() {
        System.out.println("I came to the init method");
        String host = "smtp.gmail.com";
         properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.debug", "true");
        properties.put("mail.smtp.port", "465");
//        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
//        session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(emailSender, emailPassword);
//            }
//        });
//         session = Session.getInstance(properties,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication("emkychuks062@gmail.com","yrazccvjowoxfmpq");
//                    }
//                });
//        session.setDebug(true);
//        session.setDebugOut(System.out);
    }

    @Override
    public void sendEmailForPasswordUpdateToNewUser(String email, String username,
    String defaultPassword, String urlForPasswordUpdate) {
        System.out.println("I came to the send mail method");
        init();
        try{
            MailAuthenticator authenticator = new MailAuthenticator(emailSender,emailPassword);
            Session mySession =  Session.getDefaultInstance(properties, authenticator);
            Message message = new MimeMessage(mySession);
            message.setFrom(new InternetAddress(emailSender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(welcomeMessageSubject);
            message.setSentDate(new Date());
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            welcomeMessage = welcomeMessage.replace("[[name]]",username);
            welcomeMessage = welcomeMessage.replace("[[url]]", urlForPasswordUpdate);
            welcomeMessage = welcomeMessage.replace("[[userName]]",username);
            welcomeMessage = welcomeMessage.replace("[[defaultPassword]]",defaultPassword);
            messageBodyPart.setContent(welcomeMessage, "text/html");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            System.out.println("multipart set");
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (Exception e){
            log.error("Error sending mail {} {}",e.getMessage(),e);
            throw new EmailException("Error sending mail: "+e.getMessage());
        }

    }
}
