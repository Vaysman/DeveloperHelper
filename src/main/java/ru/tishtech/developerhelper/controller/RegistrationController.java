package ru.tishtech.developerhelper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String userSave(@Valid User user, BindingResult bindingResult, Model model) {
        List<String> validationErrors = userService.getValidationErrors(user, bindingResult);
        if (validationErrors.size() > 0) {
            model.addAttribute("validationErrors", validationErrors);
            return "registration";
        } else {
            userService.userSave(user);
            return "redirect:/login";
        }
    }

    @GetMapping("/activate/{activationCode}")
    public String activate(@PathVariable String activationCode, Model model) {
        String activationResult;
        if (userService.userActivate(activationCode)) activationResult = "User successfully activated!";
        else activationResult = "Activation code is not found!";
        model.addAttribute("activationResult", activationResult);
        return "login";
    }
}
