package ru.tishtech.developerhelper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.tishtech.developerhelper.model.Role;
import ru.tishtech.developerhelper.model.User;
import ru.tishtech.developerhelper.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getActivationCode() == null) return user;
        else return null;
    }

    public void userSave(User user) {
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String text = "Hello, " + user.getUsername() + "!\n" +
                      "Welcome to DeveloperHelper!\n" +
                      "Please, click " +
                      "<a href=\"http://localhost:8080/activate/" + user.getActivationCode() + "\">here</a> " +
                      "to confirm your email";
        mailService.send(user.getEmail(), "Activation Code", text);
    }

    public void userSaveNewPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        String text = "Hello, " + user.getUsername() + "!\n" +
                      "You have just successfully changed your password!\n" +
                      "Your new password: " + newPassword;
        mailService.send(user.getEmail(), "Change password", text);
    }

    public boolean userActivate(String activationCode) {
        User user = userRepository.findByActivationCode(activationCode);
        if (user == null) return false;
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    public List<String> getUsernameErrors(User user, String username) {
        List<String> usernameErrors = new ArrayList<>();
        if (!usernameIsValid(username)) {
            usernameErrors.add("Username must have only latin letters and digits, " +
                    "also must be 3 to 16 characters long!");
        }
        if (usernameIsExists(user.getUsername())) {
            usernameErrors.add("A user is already registered with this username!");
        }
        return usernameErrors;
    }

    public List<String> getEmailErrors(User user, String email) {
        List<String> emailErrors = new ArrayList<>();
        if (!emailIsValid(email)) {
            emailErrors.add("Email is not correct!");
        }
        if (emailIsExists(user.getEmail())) {
            emailErrors.add("A user is already registered with this email address!");
        }
        return emailErrors;
    }

    public List<String> getPasswordErrors(User user, String oldPassword, String newPassword, String confirmNewPassword) {
        List<String> passwordErrors = new ArrayList<>();
        if (!user.getPassword().equals(passwordEncoder.encode(oldPassword))) {
            passwordErrors.add("Old password is not correct!");
        }
        if (user.getPassword().equals(passwordEncoder.encode(newPassword))) {
            passwordErrors.add("Old and new passwords must be different!");
        }
        if (!passwordIsValid(newPassword)) {
            passwordErrors.add("New password must have at least one lowercase letter, " +
                               "one uppercase letter, one digit, one special character, like !@#$%^&*(), " +
                               "and must be 8 to 16 characters long! All letters must be latin!");
        }
        if (!newPassword.equals(confirmNewPassword)) {
            passwordErrors.add("New password must match with confirm new password");
        }
        return passwordErrors;
    }

    public List<String> getValidationErrors(User user, BindingResult bindingResult, String confirmPassword) {
        List<String> validationErrors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            validationErrors.add("");
            return validationErrors;
        }
        if (usernameIsExists(user.getUsername())) {
            validationErrors.add("A user is already registered with this username!");
        }
        if (emailIsExists(user.getEmail())) {
            validationErrors.add("A user is already registered with this email address!");
        }
        if (!user.getPassword().equals(confirmPassword)) {
            validationErrors.add("Passwords are different!");
        }
        if (!passwordIsValid(user.getPassword())) {
            validationErrors.add("Password must have at least one lowercase letter, " +
                                 "one uppercase letter, one digit, one special character, like !@#$%^&*(), " +
                                 "and must be 8 to 16 characters long! All letters must be latin!");
        }
        return validationErrors;
    }

    private boolean passwordIsValid(String password) {
        Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()]).{8,16})");
        return pattern.matcher(password).matches();
    }

    private boolean usernameIsValid(String username) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]{3,16}$");
        return pattern.matcher(username).matches();
    }

    private boolean emailIsValid(String email) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return pattern.matcher(email).matches();
    }

    private boolean usernameIsExists(String username) {
        User userFromDatabase = userRepository.findByUsername(username);
        return userFromDatabase != null;
    }

    private boolean emailIsExists(String email) {
        User userFromDatabase = userRepository.findByEmail(email);
        return userFromDatabase != null;
    }
}
