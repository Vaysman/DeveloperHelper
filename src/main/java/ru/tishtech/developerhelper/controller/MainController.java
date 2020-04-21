package ru.tishtech.developerhelper.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tishtech.developerhelper.constants.VariableTypes;
import ru.tishtech.developerhelper.model.Variable;
import ru.tishtech.developerhelper.model.VariableStore;

@Controller
public class MainController {

    @ModelAttribute("variableTypes")
    public String[] variableTypes() {
        return VariableTypes.variableTypes;
    }

    @GetMapping("/")
    public String mainPage(Model model) {
        VariableStore variableStore = new VariableStore();
        model.addAttribute("variableStore", variableStore);
        return "main";
    }

    @PostMapping("/")
    public String generate(@RequestParam String projectName,
                           @RequestParam String groupId,
                           @RequestParam String model,
                           @ModelAttribute VariableStore variableStore) {
        for (Variable variable : variableStore.getVariables()) {
            System.out.println(variable.getType() + " - " + variable.getName());
        }
//        GeneratorService.generateFiles(projectName.toLowerCase(), groupId.toLowerCase(), model, variables);
        return "done";
    }

    @PostMapping(value = "/", params = {"addVariable"})
    public String addVariable(VariableStore variableStore) {
        variableStore.getVariables().add(new Variable());
        return "main";
    }

    @PostMapping(value = "/", params = {"removeVariable"})
    public String removeVariable(@RequestParam String removeVariable, VariableStore variableStore) {
        int variableId = Integer.parseInt(removeVariable);
        variableStore.getVariables().remove(variableId);
        return "main";
    }

}
