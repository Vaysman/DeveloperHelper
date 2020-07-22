package ru.tishtech.developerhelper.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.tishtech.developerhelper.constants.FileTypes;
import ru.tishtech.developerhelper.model.Project;
import ru.tishtech.developerhelper.model.User;
import ru.tishtech.developerhelper.model.Variable;
import ru.tishtech.developerhelper.repository.ProjectRepository;
import ru.tishtech.developerhelper.service.generator.GeneratorService;

@Controller
@RequestMapping("/user")
public class ProjectController {

  @Autowired private ProjectRepository projectRepository;

  @GetMapping("/{user}/project")
  public String projectList(
      @AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
    if (currentUser.equals(user)) {
      model.addAttribute("user", user);
      return "projectList";
    } else {
      return "accessError";
    }
  }

  @GetMapping("/{user}/project/{project}/view")
  public String projectViewPage(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      @PathVariable Project project,
      Model model) {
    if (currentUser.equals(user)) {
      model.addAttribute("user", user);
      model.addAttribute("project", project);
      return "projectView";
    } else {
      return "accessError";
    }
  }

  @GetMapping("/{user}/project/add")
  public String projectAddPage(
      @AuthenticationPrincipal User currentUser, @PathVariable User user, Model model) {
    if (currentUser.equals(user)) {
      model.addAttribute("user", user);
      model.addAttribute("project", new Project());
      return "projectAdd";
    } else {
      return "accessError";
    }
  }

  @GetMapping("/{user}/project/{project}/edit")
  public String projectEditPage(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      @PathVariable Project project,
      Model model) {
    if (currentUser.equals(user)) {
      model.addAttribute("user", user);
      model.addAttribute("project", project);
      return "projectEdit";
    } else {
      return "accessError";
    }
  }

  @PostMapping(
      value = "/{user}/project",
      params = {"addVariable"})
  public String addVariable(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      Project project,
      Model model) {
    if (currentUser.equals(user)) {
      project.getVariables().add(new Variable());
      model.addAttribute("user", user);
      if (project.getId() == null) {
        return "projectAdd";
      } else {
        return "projectEdit";
      }
    } else {
      return "accessError";
    }
  }

  @PostMapping(
      value = "/{user}/project",
      params = {"removeVariable"})
  public String removeVariable(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      @RequestParam String removeVariable,
      Project project,
      Model model) {
    if (currentUser.equals(user)) {
      int variableId = Integer.parseInt(removeVariable);
      project.getVariables().remove(variableId);
      model.addAttribute("user", user);
      if (project.getId() == null) {
        return "projectAdd";
      } else {
        return "projectEdit";
      }
    } else {
      return "accessError";
    }
  }

  @PostMapping("/{user}/project")
  public String projectSave(
      @AuthenticationPrincipal User currentUser, @PathVariable User user, Project project) {
    if (currentUser.equals(user)) {
      project.setUser(user);
      projectRepository.save(project);
      return "redirect:/user/" + user.getId() + "/project";
    } else {
      return "accessError";
    }
  }

  @GetMapping("/{user}/project/{project}/delete")
  public String projectDelete(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      @PathVariable Project project) {
    if (currentUser.equals(user)) {
      projectRepository.delete(project);
      return "redirect:/user/" + user.getId() + "/project";
    } else {
      return "accessError";
    }
  }

  @GetMapping("/{user}/project/{project}/generate")
  public String projectGenerate(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      @PathVariable Project project,
      HttpServletRequest request,
      Model model) {
    if (currentUser.equals(user)) {
      String projectPath = request.getServletContext().getRealPath("/");
      GeneratorService.generateFiles(
          project.getName(),
          project.getGroupId(),
          project.getModel(),
          project.getVariables(),
          projectPath);
      model.addAttribute("user", user);
      model.addAttribute("project", project);
      model.addAttribute("projectPath", projectPath);
      return "done";
    } else {
      return "accessError";
    }
  }

  @GetMapping(value = "/{user}/project/{project}/download", produces = "application/zip")
  public @ResponseBody byte[] projectDownload(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      @PathVariable Project project,
      @RequestParam String projectPath,
      HttpServletResponse response)
      throws IOException {
    if (currentUser.equals(user)) {
      // FIXME: try with resources
      InputStream inputStream =
          new FileInputStream(projectPath + project.getName() + FileTypes.ZIP_TYPE);
      response.addHeader(
          HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=" + project.getName() + FileTypes.ZIP_TYPE);
      return IOUtils.toByteArray(inputStream);
    } else {
      return null;
    }
  }
}
