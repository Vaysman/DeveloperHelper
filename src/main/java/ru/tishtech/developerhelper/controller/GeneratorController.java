package ru.tishtech.developerhelper.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tishtech.developerhelper.constants.FileTypes;
import ru.tishtech.developerhelper.constants.VariableTypes;
import ru.tishtech.developerhelper.model.Project;
import ru.tishtech.developerhelper.model.Variable;
import ru.tishtech.developerhelper.service.generator.GeneratorService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/generator")
public class GeneratorController {

    @ModelAttribute("variableTypes")
    public String[] variableTypes() {
        return VariableTypes.variableTypes;
    }

    @GetMapping
    public String generatorPage(Model model) {
        Project project = new Project();
        model.addAttribute("project", project);
        return "main";
    }

    @PostMapping(params = {"addVariable"})
    public String addVariable(Project project) {
        project.getVariables().add(new Variable());
        return "main";
    }

    @PostMapping(params = {"removeVariable"})
    public String removeVariable(@RequestParam String removeVariable, Project project) {
        int variableId = Integer.parseInt(removeVariable);
        project.getVariables().remove(variableId);
        return "main";
    }

    @PostMapping
    public String generate(@Valid Project project, BindingResult bindingResult,
                           HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) {
            return "main";
        } else {
            project.setPath(request.getServletContext().getRealPath(""));
            GeneratorService.generateFiles(project.getName(), project.getGroupId(), project.getModel(),
                    project.getVariables(), project.getPath());
            model.addAttribute("project", project);
        }
        return "done";
    }

    @GetMapping(value = "/download", produces = "application/zip")
    public @ResponseBody byte[] download(@RequestParam String projectName,
                                         @RequestParam String projectPath,
                                         HttpServletResponse response) throws IOException {
        InputStream inputStream = new FileInputStream(projectPath + projectName + FileTypes.ZIP_TYPE);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + projectName + FileTypes.ZIP_TYPE);
        return IOUtils.toByteArray(inputStream);
    }

}
