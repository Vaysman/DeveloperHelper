package ru.tishtech.developerhelper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import ru.tishtech.developerhelper.model.Role;
import ru.tishtech.developerhelper.model.User;
import ru.tishtech.developerhelper.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
        if (!StringUtils.isEmpty(user.getEmail())) {
            String text = "Hello, " + user.getUsername() + "!\n" +
                    "Welcome to DeveloperHelper!\n" +
                    "Please, visit next link to confirm your email: " +
                    "http://localhost:8080/activate/" + user.getActivationCode();
            mailService.send(user.getEmail(), "Activation Code", text);
        }
    }

    public boolean userActivate(String activationCode) {
        User user = userRepository.findByActivationCode(activationCode);
        if (user == null) return false;
        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    public List<String> getValidationErrors(User user, BindingResult bindingResult) {
        List<String> errors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            errors.add("");
            return errors;
        }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            errors.add("A user is already registered with this username!");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            errors.add("A user is already registered with this email address!");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            errors.add("Passwords are different!");
        }
        return errors;
    }
}
