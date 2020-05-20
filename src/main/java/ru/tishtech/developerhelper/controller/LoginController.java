package ru.tishtech.developerhelper.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.tishtech.developerhelper.model.User;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value = {"/", "/hello"})
    public String mainPage(@AuthenticationPrincipal User currentUser, Model model) {
        if (currentUser == null) {
            return "home";
        } else {
            model.addAttribute("user", currentUser);
            return "hello";
        }
    }
}
