package ru.tishtech.developerhelper.service.generator;

import java.util.ArrayList;
import java.util.List;

public class PomGeneratorService {

    public static List<String> generatePomData(List<String> data, String projectName, String groupId) {
        List<String> dependencies = new ArrayList<>();
        String dependenciesString = "";
        for (String dependency : dependencies) {
            dependenciesString += dependency;
        }
        boolean groupIdWasFound = false;
        boolean artifactIdWasFound = false;
        boolean dependenciesWereFound = false;
        for (int i = 0; i < data.size(); i++) {
            if (!groupIdWasFound && data.get(i).contains("{groupId}")) {
                data.set(i, data.get(i).replace("{groupId}", groupId));
                groupIdWasFound = true;
            } else if (!artifactIdWasFound && data.get(i).contains("{artifactId}")) {
                data.set(i, data.get(i).replace("{artifactId}", projectName));
                artifactIdWasFound = true;
            } else if (!dependenciesWereFound && data.get(i).contains("{dependencies}")) {
                data.set(i, data.get(i).replace("{dependencies}", dependenciesString));
                dependenciesWereFound = true;
            }
        }
        return data;
    }
}
