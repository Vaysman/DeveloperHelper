package ru.tishtech.developerhelper.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tishtech.developerhelper.model.User;
import ru.tishtech.developerhelper.service.UserService;

@Controller
public class LoginController {

  @Autowired private UserService userService;

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

  @GetMapping("/password/forgot")
  public String forgotPasswordPage() {
    return "forgotPassword";
  }

  @PostMapping("/password/forgot")
  public String forgotPassword(@RequestParam String username, Model model) {
    model.addAttribute("forgotPasswordInfo", userService.userForgotPassword(username));
    return "forgotPassword";
  }

  @GetMapping("/password/{forgotPasswordCode}")
  public String createNewPasswordPage(@PathVariable String forgotPasswordCode, Model model) {
    if (userService.userCreateNewPassword(forgotPasswordCode)) {
      model.addAttribute("forgotPasswordCode", forgotPasswordCode);
      return "createNewPassword";
    } else {
      model.addAttribute("forgotPasswordInfo", "Oops, something went wrong!");
      return "forgotPassword";
    }
  }

  @PostMapping("/password/{forgotPasswordCode}/create")
  public String createNewPassword(
      @PathVariable String forgotPasswordCode,
      @RequestParam String newPassword,
      @RequestParam String confirmNewPassword,
      Model model) {
    List<String> passwordErrors =
        userService.getPasswordErrors(null, null, newPassword, confirmNewPassword);
    if (!passwordErrors.isEmpty()) {
      model.addAttribute("passwordErrors", passwordErrors);
      model.addAttribute("forgotPasswordCode", forgotPasswordCode);
      return "createNewPassword";
    } else {
      userService.userSaveNewPassword(null, newPassword, forgotPasswordCode);
      model.addAttribute("createNewPasswordResult", "Password changed successfully");
      return "login";
    }
  }
}
