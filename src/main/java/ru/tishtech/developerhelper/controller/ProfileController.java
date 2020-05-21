package ru.tishtech.developerhelper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.tishtech.developerhelper.model.User;
import ru.tishtech.developerhelper.repository.UserRepository;
import ru.tishtech.developerhelper.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/user")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/{user}/profile")
    public String profilePage(@AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
        if (currentUser.equals(user)) {
            model.addAttribute("user", user);
            return "profile";
        } else {
            return "accessError";
        }
    }

    @GetMapping("/{user}/profile/edit")
    public String profileEditPage(@AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
        if (currentUser.equals(user)) {
            model.addAttribute("user", user);
            return "profileEdit";
        } else {
            return "accessError";
        }
    }

    @GetMapping("/{user}/profile/password/edit")
    public String profileEditPasswordPage(@AuthenticationPrincipal User currentUser, @PathVariable User user,
                                          Model model) {
        if (currentUser.equals(user)) {
            model.addAttribute("user", user);
            return "profileEditPassword";
        } else {
            return "accessError";
        }
    }

    @PostMapping("/{user}/profile/password")
    public String profileSaveNewPassword(@AuthenticationPrincipal User currentUser, @PathVariable User user,
                                         @RequestParam String oldPassword, @RequestParam String newPassword,
                                         @RequestParam String confirmNewPassword,
                                         Model model) {
        if (currentUser.equals(user)) {
            List<String> passwordErrors = userService.getPasswordErrors(user, oldPassword, newPassword, confirmNewPassword);
            if (!passwordErrors.isEmpty()) {
                model.addAttribute("passwordErrors", passwordErrors);
                return "profileEditPassword";
            } else {
                userService.userSaveNewPassword(user, newPassword);
                model.addAttribute("user", user);
                return "passwordSuccess";
            }
        } else {
            return "accessError";
        }
    }

    @PostMapping("/{user}/profile")
    public String profileSave(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
        if (currentUser.equals(user)) {
            userRepository.save(user);
            return "redirect:/user/" + user.getId() + "/profile";
        } else {
            return "accessError";
        }
    }
}
