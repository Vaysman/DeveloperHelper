package ru.tishtech.developerhelper.service;

import org.apache.commons.io.FileUtils;
import ru.tishtech.developerhelper.constants.FileNames;
import ru.tishtech.developerhelper.constants.FilePaths;
import ru.tishtech.developerhelper.constants.FileTypes;
import ru.tishtech.developerhelper.model.Variable;
import ru.tishtech.developerhelper.util.ZipUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GeneratorService {

    public static void generateFiles(String projectName, String groupId, String model,
                                     List<Variable> variables, String resultPath) {
        String[] groupIdParts = groupId.split("\\.");
        String capitalModel = CorrectorService.toCapitalString(model);
        String smallModel = CorrectorService.toSmallString(model);

        List<String> pomData = PomGeneratorService.generatePomData(
                ReaderService.readData(FileNames.POM_NAME + FileTypes.TXT_TYPE),
                projectName, groupId);
        String pomPath = resultPath + projectName;
        WriterService.writeData(pomData, FileNames.POM_NAME + FileTypes.XML_TYPE, pomPath);

        List<String> applicationData = ApplicationGeneratorService.generateApplicationData(
                ReaderService.readData(FileNames.APPLICATION_NAME + FileTypes.TXT_TYPE),
                projectName, groupId);
        String applicationPath = resultPath + projectName + "/" + FilePaths.SRC_MAIN_JAVA_DIR +
                groupIdParts[0] + "/" + groupIdParts[1] + "/" + projectName;
        WriterService.writeData(applicationData, FileNames.APPLICATION_NAME + FileTypes.JAVA_TYPE, applicationPath);

        List<String> modelData = ModelGeneratorService.generateModelData(
                ReaderService.readData(FileNames.MODEL_NAME + FileTypes.TXT_TYPE),
                variables, projectName, groupId, capitalModel);
        String modelPath = resultPath + projectName + "/" + FilePaths.SRC_MAIN_JAVA_DIR +
                groupIdParts[0] + "/" + groupIdParts[1] + "/" + projectName + "/" + FilePaths.MODEL_DIR;
        WriterService.writeData(modelData, capitalModel + FileTypes.JAVA_TYPE, modelPath);

        List<String> repositoryData = RepositoryGeneratorService.generateRepositoryData(
                ReaderService.readData(FileNames.REPOSITORY_NAME + FileTypes.TXT_TYPE),
                projectName, groupId, capitalModel);
        String repositoryPath = resultPath + projectName + "/" + FilePaths.SRC_MAIN_JAVA_DIR +
                groupIdParts[0] + "/" + groupIdParts[1] + "/" + projectName + "/" + FilePaths.REPOSITORY_DIR;
        WriterService.writeData(repositoryData,
                capitalModel + FileNames.REPOSITORY_NAME + FileTypes.JAVA_TYPE, repositoryPath);

        List<String> controllerData = ControllerGeneratorService.generateControllerData(
                ReaderService.readData(FileNames.CONTROLLER_NAME + FileTypes.TXT_TYPE),
                projectName, groupId, capitalModel, smallModel);
        String controllerPath = resultPath + projectName + "/" + FilePaths.SRC_MAIN_JAVA_DIR +
                groupIdParts[0] + "/" + groupIdParts[1] + "/" + projectName + "/" + FilePaths.CONTROLLER_DIR;
        WriterService.writeData(controllerData,
                capitalModel + FileNames.CONTROLLER_NAME + FileTypes.JAVA_TYPE, controllerPath);

        String modelPagesPath = resultPath + projectName + "/" + FilePaths.SRC_MAIN_RESOURCES_TEMPLATES_DIR;

        List<String> modelListPageData = ModelListPageGeneratorService.generateModelListPageData(
                ReaderService.readData(FileNames.MODEL_LIST_NAME + FileTypes.TXT_TYPE),
                variables, capitalModel, smallModel);
        WriterService.writeData(modelListPageData,
                smallModel + FileNames.LIST_NAME + FileTypes.HTML_TYPE, modelPagesPath);

        List<String> modelAddPageData = ModelAddAndEditPagesGeneratorService.generateModelAddAndEditPagesData(
                ReaderService.readData(FileNames.MODEL_ADD_NAME + FileTypes.TXT_TYPE),
                variables, projectName, groupId, capitalModel, smallModel);
        WriterService.writeData(modelAddPageData,
                smallModel + FileNames.ADD_NAME + FileTypes.HTML_TYPE, modelPagesPath);

        List<String> modelEditPageData = ModelAddAndEditPagesGeneratorService.generateModelAddAndEditPagesData(
                ReaderService.readData(FileNames.MODEL_EDIT_NAME + FileTypes.TXT_TYPE),
                variables, projectName, groupId, capitalModel, smallModel);
        WriterService.writeData(modelEditPageData,
                smallModel + FileNames.EDIT_NAME + FileTypes.HTML_TYPE, modelPagesPath);

        List<String> applicationPropsData = ReaderService.readData(
                FileNames.APPLICATION_PROPS_NAME + FileTypes.TXT_TYPE);
        String applicationPropsPath = resultPath + projectName + "/" + FilePaths.SRC_MAIN_RESOURCES_DIR;
        WriterService.writeData(applicationPropsData,
                FileNames.SMALL_APPLICATION_NAME + FileTypes.PROPERTIES_TYPE, applicationPropsPath);

        ZipUtil.zip(resultPath + projectName, resultPath + projectName + FileTypes.ZIP_TYPE);

        File folderForDelete = new File(resultPath + projectName);
        if (folderForDelete.exists()) {
            try {
                FileUtils.deleteDirectory(folderForDelete);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
