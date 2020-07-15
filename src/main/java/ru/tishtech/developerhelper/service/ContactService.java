package ru.tishtech.developerhelper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ContactService {

    @Value("${contact.to.email}")
    private String toEmail;

    @Autowired
    private MailService mailService;

    public void contactToEmail(String name, String email, String text) {
        String contactData = "You received a new message!\n" +
                             "Name: " + name + "\n" +
                             "Email: " + email + "\n" +
                             "Text: " + text;
        mailService.send(toEmail, "Message", contactData);
    }

    public List<String> getContactErrors(String name, String email, String text) {
        List<String> contactErrors = new ArrayList<>();
        if (name.trim().equals("")) {
            contactErrors.add("Name is required!");
        }
        if (!emailIsValid(email)) {
            contactErrors.add("Email is not correct!");
        }
        if (text.trim().equals("")) {
            contactErrors.add("Text is required!");
        }
        return contactErrors;
    }

    private boolean emailIsValid(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return pattern.matcher(email).matches();
    }
}
