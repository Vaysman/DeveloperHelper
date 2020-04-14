package ru.tishtech.developerhelper.service;

import ru.tishtech.developerhelper.constants.FileNames;
import ru.tishtech.developerhelper.constants.FilePaths;
import ru.tishtech.developerhelper.constants.FileTypes;
import ru.tishtech.developerhelper.model.Variable;

import java.util.List;

public class GeneratorService {

    public static void generateFiles(String projectName, String groupId, String model, List<Variable> variables) {
        String[] groupIdParts = groupId.split("\\.");

        List<String> pomData = PomGeneratorService.generatePomData(
                ReaderService.readData(FileNames.POM_NAME + FileTypes.TXT_TYPE),
                projectName, groupId);
        WriterService.writeData(pomData, FileNames.POM_NAME + FileTypes.XML_TYPE, projectName);

        List<String> applicationData = ApplicationGeneratorService.generateApplicationData(
                ReaderService.readData(FileNames.APPLICATION_NAME + FileTypes.TXT_TYPE),
                projectName, groupId);
        String applicationPath = projectName + "/" + FilePaths.SRC_MAIN_JAVA_DIR +
                groupIdParts[0] + "/" + groupIdParts[1] + "/" + projectName;
        WriterService.writeData(applicationData, FileNames.APPLICATION_NAME + FileTypes.JAVA_TYPE, applicationPath);

        List<String> modelData = ModelGeneratorService.generateModelData(
                ReaderService.readData(FileNames.MODEL_NAME + FileTypes.TXT_TYPE),
                variables, projectName, groupId, model);
        String modelPath = projectName + "/" + FilePaths.SRC_MAIN_JAVA_DIR +
                groupIdParts[0] + "/" + groupIdParts[1] + "/" + projectName + "/" + FilePaths.MODEL_DIR;
        WriterService.writeData(modelData, model + FileTypes.JAVA_TYPE, modelPath);

        List<String> repositoryData = RepositoryGeneratorService.generateRepositoryData(
                ReaderService.readData(FileNames.REPOSITORY_NAME + FileTypes.TXT_TYPE),
                projectName, groupId, model);
        String repositoryPath = projectName + "/" + FilePaths.SRC_MAIN_JAVA_DIR +
                groupIdParts[0] + "/" + groupIdParts[1] + "/" + projectName + "/" + FilePaths.REPOSITORY_DIR;
        WriterService.writeData(repositoryData,
                model + FileNames.REPOSITORY_NAME + FileTypes.JAVA_TYPE, repositoryPath);

        List<String> controllerData = ControllerGeneratorService.generateControllerData(
                ReaderService.readData(FileNames.CONTROLLER_NAME + FileTypes.TXT_TYPE),
                projectName, groupId, model);
        String controllerPath = projectName + "/" + FilePaths.SRC_MAIN_JAVA_DIR +
                groupIdParts[0] + "/" + groupIdParts[1] + "/" + projectName + "/" + FilePaths.CONTROLLER_DIR;
        WriterService.writeData(controllerData,
                model + FileNames.CONTROLLER_NAME + FileTypes.JAVA_TYPE, controllerPath);

        List<String> modelListPageData = ModelListPageGeneratorService.generateModelListPageData(
                ReaderService.readData(FileNames.MODEL_LIST_NAME + FileTypes.TXT_TYPE),
                variables, model);
        String modelListPagePath = projectName + "/" + FilePaths.SRC_MAIN_RESOURCES_TEMPLATES_DIR;
        WriterService.writeData(modelListPageData,
                FileNames.MODEL_LIST_NAME + FileTypes.HTML_TYPE, modelListPagePath);
    }

}
