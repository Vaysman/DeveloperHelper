package ru.tishtech.developerhelper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tishtech.developerhelper.model.User;
import ru.tishtech.developerhelper.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/{user}/profile/editpassword")
    public String profileEditPasswordPage(@AuthenticationPrincipal User currentUser, @PathVariable User user,
                                          Model model) {
        if (currentUser.equals(user)) {
            model.addAttribute("user", user);
            return "profileEditPassword";
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
