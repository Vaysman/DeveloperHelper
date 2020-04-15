package ru.tishtech.developerhelper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tishtech.developerhelper.model.Variable;
import ru.tishtech.developerhelper.service.GeneratorService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @PostMapping("/")
    public String generate(@RequestParam String projectName,
                           @RequestParam String groupId,
                           @RequestParam String model,
                           @RequestParam String name1,
                           @RequestParam String type1,
                           @RequestParam String name2,
                           @RequestParam String type2) {
        List<Variable> variables = new ArrayList<>();
        variables.add(new Variable(name1, type1));
        variables.add(new Variable(name2, type2));
        GeneratorService.generateFiles(projectName.toLowerCase(), groupId.toLowerCase(), model, variables);
        return "done";
    }

}
