package ru.tishtech.developerhelper.service.generator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import ru.tishtech.developerhelper.constants.FileNames;
import ru.tishtech.developerhelper.constants.FilePaths;
import ru.tishtech.developerhelper.constants.FileTypes;
import ru.tishtech.developerhelper.model.Variable;
import ru.tishtech.developerhelper.service.CorrectorService;
import ru.tishtech.developerhelper.service.ReaderService;
import ru.tishtech.developerhelper.service.WriterService;
import ru.tishtech.developerhelper.util.ZipUtil;

// FIXME: switch to any templating engine
public class GeneratorService {

  public static void generateFiles(
      String projectName,
      String groupId,
      String model,
      List<Variable> variables,
      String projectPath) {
    ReaderService readerService = new ReaderService();
    String[] groupIdParts = groupId.split("\\.");
    String capitalModel = CorrectorService.toCapitalString(model);
    String smallModel = CorrectorService.toSmallString(model);
    String groupIdPath = "";
    // FIXME: use StringBuilder
    for (String groupIdPart : groupIdParts) {
      groupIdPath += groupIdPart + "/";
    }

    List<String> pomData =
        PomGeneratorService.generatePomData(
            readerService.readData(FileNames.POM_NAME + FileTypes.TXT_TYPE), projectName, groupId);
    String pomPath = projectPath + projectName;
    WriterService.writeData(pomData, FileNames.POM_NAME + FileTypes.XML_TYPE, pomPath);

    List<String> applicationData =
        ApplicationGeneratorService.generateApplicationData(
            readerService.readData(FileNames.APPLICATION_NAME + FileTypes.TXT_TYPE),
            projectName,
            groupId);

    String applicationPath =
        projectPath + projectName + "/" + FilePaths.SRC_MAIN_JAVA_DIR + groupIdPath + projectName;
    WriterService.writeData(
        applicationData, FileNames.APPLICATION_NAME + FileTypes.JAVA_TYPE, applicationPath);

    List<String> modelData =
        ModelGeneratorService.generateModelData(
            readerService.readData(FileNames.MODEL_NAME + FileTypes.TXT_TYPE),
            variables,
            projectName,
            groupId,
            capitalModel);
    String modelPath =
        projectPath
            + projectName
            + "/"
            + FilePaths.SRC_MAIN_JAVA_DIR
            + groupIdPath
            + projectName
            + "/"
            + FilePaths.MODEL_DIR;
    WriterService.writeData(modelData, capitalModel + FileTypes.JAVA_TYPE, modelPath);

    List<String> repositoryData =
        RepositoryGeneratorService.generateRepositoryData(
            readerService.readData(FileNames.REPOSITORY_NAME + FileTypes.TXT_TYPE),
            projectName,
            groupId,
            capitalModel);
    String repositoryPath =
        projectPath
            + projectName
            + "/"
            + FilePaths.SRC_MAIN_JAVA_DIR
            + groupIdPath
            + projectName
            + "/"
            + FilePaths.REPOSITORY_DIR;
    WriterService.writeData(
        repositoryData,
        capitalModel + FileNames.REPOSITORY_NAME + FileTypes.JAVA_TYPE,
        repositoryPath);

    List<String> controllerData =
        ControllerGeneratorService.generateControllerData(
            readerService.readData(FileNames.CONTROLLER_NAME + FileTypes.TXT_TYPE),
            projectName,
            groupId,
            capitalModel,
            smallModel);
    String controllerPath =
        projectPath
            + projectName
            + "/"
            + FilePaths.SRC_MAIN_JAVA_DIR
            + groupIdPath
            + projectName
            + "/"
            + FilePaths.CONTROLLER_DIR;
    WriterService.writeData(
        controllerData,
        capitalModel + FileNames.CONTROLLER_NAME + FileTypes.JAVA_TYPE,
        controllerPath);

    String modelPagesPath =
        projectPath + projectName + "/" + FilePaths.SRC_MAIN_RESOURCES_TEMPLATES_DIR;

    List<String> modelListPageData =
        ModelListPageGeneratorService.generateModelListPageData(
            readerService.readData(FileNames.MODEL_LIST_NAME + FileTypes.TXT_TYPE),
            variables,
            capitalModel,
            smallModel);
    WriterService.writeData(
        modelListPageData, smallModel + FileNames.LIST_NAME + FileTypes.HTML_TYPE, modelPagesPath);

    List<String> modelAddPageData =
        ModelAddAndEditPagesGeneratorService.generateModelAddAndEditPagesData(
            readerService.readData(FileNames.MODEL_ADD_NAME + FileTypes.TXT_TYPE),
            variables,
            capitalModel,
            smallModel);
    WriterService.writeData(
        modelAddPageData, smallModel + FileNames.ADD_NAME + FileTypes.HTML_TYPE, modelPagesPath);

    List<String> modelEditPageData =
        ModelAddAndEditPagesGeneratorService.generateModelAddAndEditPagesData(
            readerService.readData(FileNames.MODEL_EDIT_NAME + FileTypes.TXT_TYPE),
            variables,
            capitalModel,
            smallModel);
    WriterService.writeData(
        modelEditPageData, smallModel + FileNames.EDIT_NAME + FileTypes.HTML_TYPE, modelPagesPath);

    List<String> applicationPropsData =
        readerService.readData(FileNames.APPLICATION_PROPS_NAME + FileTypes.TXT_TYPE);
    String applicationPropsPath =
        projectPath + projectName + "/" + FilePaths.SRC_MAIN_RESOURCES_DIR;
    WriterService.writeData(
        applicationPropsData,
        FileNames.SMALL_APPLICATION_NAME + FileTypes.PROPERTIES_TYPE,
        applicationPropsPath);

    ZipUtil.zip(projectPath + projectName, projectPath + projectName + FileTypes.ZIP_TYPE);

    File folderForDelete = new File(projectPath + projectName);
    if (folderForDelete.exists()) {
      try {
        FileUtils.deleteDirectory(folderForDelete);
      } catch (IOException e) {
        // FIXME: use logging
        e.printStackTrace();
      }
    }
  }
}
