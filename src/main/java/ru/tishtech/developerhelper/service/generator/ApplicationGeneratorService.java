package ru.tishtech.developerhelper.service.generator;

import java.util.List;

public class ApplicationGeneratorService {

    public static List<String> generateApplicationData(List<String> data, String projectName, String groupId) {
        data.set(0, data.get(0).replace("{groupId}", groupId));
        data.set(0, data.get(0).replace("{projectName}", projectName));
        return data;
    }
}
