package ru.tishtech.developerhelper.service;

import ru.tishtech.developerhelper.constants.FileNames;
import ru.tishtech.developerhelper.constants.FilePaths;
import ru.tishtech.developerhelper.constants.FileTypes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GeneratorService {

    private String projectName;
    private String groupId;

    private List<String> data;

    public GeneratorService(String projectName, String groupId) {
        this.projectName = projectName;
        this.groupId = groupId;
    }

    public void generateFiles() {
        List<String> pomData = PomGeneratorService.generatePomData(
                readData(FileNames.POM_NAME + FileTypes.TXT_TYPE),
                projectName, groupId);
        writeData(pomData, FileNames.POM_NAME + FileTypes.XML_TYPE, projectName);

        List<String> applicationData = ApplicationGeneratorService.generateApplicationData(
                readData(FileNames.APPLICATION_NAME + FileTypes.TXT_TYPE),
                projectName, groupId);
        String[] groupIdParts = groupId.split("\\.");
        String applicationPath = projectName + "/" + FilePaths.SRC_MAIN_JAVA_DIR +
                groupIdParts[0] + "/" + groupIdParts[1] + "/" + projectName;
        writeData(applicationData, FileNames.APPLICATION_NAME + FileTypes.JAVA_TYPE, applicationPath);
    }

    private void writeData(List<String> data, String fileName, String folder) {
        File filePath = new File(FilePaths.FILES_TO_DIR + folder);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filePath, fileName)));
            for (String line : data) {
                bufferedWriter.write(line + "\n");
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readData(String fileName) {
        List<String> data = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader(new File(FilePaths.FILES_FROM_DIR + fileName)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                data.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
