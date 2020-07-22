package ru.tishtech.developerhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tishtech.developerhelper.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {}
