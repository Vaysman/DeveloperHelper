package ru.tishtech.developerhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tishtech.developerhelper.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String activationCode);
}
