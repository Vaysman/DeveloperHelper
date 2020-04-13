package ru.tishtech.developerhelper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tishtech.developerhelper.service.GeneratorService;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @PostMapping("/")
    public String generate(@RequestParam String projectName,
                           @RequestParam String groupId) {
        GeneratorService generatorService = new GeneratorService(projectName, groupId);
        generatorService.generateFiles();
        return "done";
    }

}
