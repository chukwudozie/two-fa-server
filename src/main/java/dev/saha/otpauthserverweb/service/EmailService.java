package dev.saha.otpauthserverweb.service;

public interface EmailService {

    void sendEmailForPasswordUpdateToNewUser(String email, String username, String defaultPassword, String urlForPasswordUpdate);
}
