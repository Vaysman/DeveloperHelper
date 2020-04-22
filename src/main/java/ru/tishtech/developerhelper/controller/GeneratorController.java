package ru.tishtech.developerhelper.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.tishtech.developerhelper.constants.VariableTypes;
import ru.tishtech.developerhelper.model.Variable;
import ru.tishtech.developerhelper.model.VariableStore;
import ru.tishtech.developerhelper.service.GeneratorService;
import ru.tishtech.developerhelper.util.ZipUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/generator")
public class GeneratorController {

    private String downloadDir;

    @ModelAttribute("variableTypes")
    public String[] variableTypes() {
        return VariableTypes.variableTypes;
    }

    @GetMapping
    public String generatorPage(Model model) {
        VariableStore variableStore = new VariableStore();
        model.addAttribute("variableStore", variableStore);
        return "main";
    }

    @PostMapping(params = {"addVariable"})
    public String addVariable(VariableStore variableStore) {
        variableStore.getVariables().add(new Variable());
        return "main";
    }

    @PostMapping(params = {"removeVariable"})
    public String removeVariable(@RequestParam String removeVariable, VariableStore variableStore) {
        int variableId = Integer.parseInt(removeVariable);
        variableStore.getVariables().remove(variableId);
        return "main";
    }

    @PostMapping
    public String generate(@RequestParam String projectName,
                           @RequestParam String groupId,
                           @RequestParam String model,
                           @ModelAttribute VariableStore variableStore, HttpServletRequest request) {
        String resultPath = request.getServletContext().getRealPath("");
        GeneratorService.generateFiles(projectName.toLowerCase(), groupId.toLowerCase(),
                model, variableStore.getVariables(), resultPath);
        System.out.println(resultPath);
        downloadDir = resultPath;
        return "done";
    }

    @GetMapping(value = "/download", produces = "application/zip")
    public @ResponseBody byte[] download(HttpServletResponse response) throws IOException {
        InputStream inputStream = new FileInputStream(downloadDir + "project1.zip");
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=project1.zip");
        return IOUtils.toByteArray(inputStream);
    }

}
