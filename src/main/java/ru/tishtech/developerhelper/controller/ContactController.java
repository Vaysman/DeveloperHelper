package ru.tishtech.developerhelper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tishtech.developerhelper.service.ContactService;

import java.util.List;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @PostMapping("/contact")
    public String contact(@RequestParam String name, @RequestParam String email,
                          @RequestParam String text, Model model) {
        List<String> contactErrors = contactService.getContactErrors(name, email, text);
        if (!contactErrors.isEmpty()) {
            model.addAttribute("contactErrors", contactErrors);
        } else {
            contactService.contactToEmail(name, email, text);
            model.addAttribute("sendResult","Your message has been sent!");
        }
        return "contact";
    }
}
