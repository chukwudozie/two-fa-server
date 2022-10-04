package dev.saha.otpauthserverweb.service.impl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {
private final String strUser;
private final String strPwd;

    @Override
public PasswordAuthentication getPasswordAuthentication() {
    String username = this.strUser;
    String password = this.strPwd;
    if ((username != null) && (username.length() > 0) && (password != null) && (password.length() > 0)) {
        return new PasswordAuthentication(username, password);
    }
    return null;
}
public MailAuthenticator(String user, String password) {
    this.strUser = user;
    this.strPwd = password;
}
}