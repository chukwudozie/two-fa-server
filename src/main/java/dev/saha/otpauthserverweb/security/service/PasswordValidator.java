package dev.saha.otpauthserverweb.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class PasswordValidator {
    private final Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_REGEX = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%.!^&*()]).{6,20})";

    public PasswordValidator()
    {
        pattern = Pattern.compile(PASSWORD_REGEX);
    }

    public boolean validate(final String password)
    {
        matcher = pattern.matcher(password);
        log.info("Does the provided password match the required pattern: {}",matcher.matches());
        return matcher.matches();
    }

}