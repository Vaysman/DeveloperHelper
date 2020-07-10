package ru.tishtech.developerhelper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tishtech.developerhelper.model.Role;
import ru.tishtech.developerhelper.model.User;
import ru.tishtech.developerhelper.repository.UserRepository;
import ru.tishtech.developerhelper.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String userSave(@Valid User user, BindingResult bindingResult,
                           @RequestParam String confirmPassword, Model model) {
        List<String> validationErrors = userService.getValidationErrors(user, bindingResult, confirmPassword);
        if (!validationErrors.isEmpty()) {
            boolean zeroElementHasSize = !validationErrors.get(0).isEmpty();
            model.addAttribute("validationErrors", validationErrors);
            model.addAttribute("zeroElementHasSize", zeroElementHasSize);
            return "registration";
        } else {
            userService.userSave(user);
            model.addAttribute("checkYourEmail", "Check your email");
            return "login";
        }
    }

    @GetMapping("/activate/{activationCode}")
    public String userActivate(@PathVariable String activationCode, Model model) {
        String activationResult;
        if (userService.userActivate(activationCode)) activationResult = "User successfully activated!";
        else activationResult = "Activation code is not found!";
        model.addAttribute("activationResult", activationResult);
        return "login";
    }
}
