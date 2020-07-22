package ru.tishtech.developerhelper.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tishtech.developerhelper.model.User;
import ru.tishtech.developerhelper.service.UserService;

@Controller
@RequestMapping("/user")
public class ProfileController {

  @Autowired private UserService userService;

  @GetMapping("/{user}/profile")
  public String profilePage(
      @AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
    if (currentUser.equals(user)) {
      model.addAttribute("user", user);
      return "profile";
    } else {
      return "accessError";
    }
  }

  @GetMapping("/{user}/profile/edit")
  public String profileEditPage(
      @AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
    if (currentUser.equals(user)) {
      model.addAttribute("user", user);
      return "profileEdit";
    } else {
      return "accessError";
    }
  }

  @GetMapping("/{user}/profile/username/edit")
  public String profileEditUsernamePage(
      @AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
    if (currentUser.equals(user)) {
      model.addAttribute("user", user);
      return "profileEditUsername";
    } else {
      return "accessError";
    }
  }

  @GetMapping("/{user}/profile/email/edit")
  public String profileEditEmailPage(
      @AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
    if (currentUser.equals(user)) {
      model.addAttribute("user", user);
      return "profileEditEmail";
    } else {
      return "accessError";
    }
  }

  @GetMapping("/{user}/profile/password/edit")
  public String profileEditPasswordPage(
      @AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
    if (currentUser.equals(user)) {
      model.addAttribute("user", user);
      return "profileEditPassword";
    } else {
      return "accessError";
    }
  }

  @PostMapping("/{user}/profile/username")
  public String profileSaveNewUsername(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      @RequestParam String username,
      Model model) {
    if (currentUser.equals(user)) {
      List<String> usernameErrors = userService.getUsernameErrors(username);
      if (!usernameErrors.isEmpty()) {
        model.addAttribute("usernameErrors", usernameErrors);
        return "profileEditUsername";
      } else {
        userService.userSaveNewUsername(user, username);
        model.addAttribute("user", user);
        model.addAttribute("mutable", "Username");
        return "success";
      }
    } else {
      return "accessError";
    }
  }

  @PostMapping("/{user}/profile/email")
  public String profileSaveNewEmail(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      @RequestParam String email,
      Model model) {
    if (currentUser.equals(user)) {
      List<String> emailErrors = userService.getEmailErrors(email);
      if (!emailErrors.isEmpty()) {
        model.addAttribute("emailErrors", emailErrors);
        return "profileEditEmail";
      } else {
        userService.userSaveNewEmail(user, email);
        model.addAttribute("user", user);
        return "checkEmail";
      }
    } else {
      return "accessError";
    }
  }

  @GetMapping("/{user}/profile/email/{emailAntiSpam}/activate/{activationCode}")
  public String profileActivateNewEmail(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      @PathVariable String emailAntiSpam,
      @PathVariable String activationCode,
      Model model) {
    if (currentUser.equals(user)) {
      String email =
          emailAntiSpam
              .replace(user.getActivationCode() + "at", "@")
              .replaceAll(user.getActivationCode() + "dot", ".");
      if (userService.userActivateNewEmail(user, email, activationCode)) {
        model.addAttribute("mutable", "Email");
        return "success";
      } else {
        return "fail";
      }
    } else {
      return "accessError";
    }
  }

  @PostMapping("/{user}/profile/password")
  public String profileSaveNewPassword(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      @RequestParam String oldPassword,
      @RequestParam String newPassword,
      @RequestParam String confirmNewPassword,
      Model model) {
    if (currentUser.equals(user)) {
      List<String> passwordErrors =
          userService.getPasswordErrors(user, oldPassword, newPassword, confirmNewPassword);
      if (!passwordErrors.isEmpty()) {
        model.addAttribute("passwordErrors", passwordErrors);
        return "profileEditPassword";
      } else {
        userService.userSaveNewPassword(user, newPassword, null);
        model.addAttribute("user", user);
        model.addAttribute("mutable", "Password");
        return "success";
      }
    } else {
      return "accessError";
    }
  }

  @GetMapping("/{user}/profile/delete")
  public String profileDelete(@AuthenticationPrincipal User currentUser, @PathVariable User user) {
    if (currentUser.equals(user)) {
      userService.userDelete(user);
      return "home";
    } else {
      return "accessError";
    }
  }
}
