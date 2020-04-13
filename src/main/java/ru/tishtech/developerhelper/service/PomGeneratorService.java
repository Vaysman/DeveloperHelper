package ru.tishtech.developerhelper.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PomGeneratorService {

    public static List<String> generatePomData(List<String> data, String projectName, String groupId) {
        List<String> dependencies = new ArrayList<>();
        dependencies.add("<dependency>\n" +
                "    <groupId>org.springframework.boot</groupId>\n" +
                "    <artifactId>spring-boot-starter-thymeleaf</artifactId>\n" +
                "</dependency>\n");
        dependencies.add("<dependency>\n" +
                "    <groupId>org.springframework.boot</groupId>\n" +
                "    <artifactId>spring-boot-starter-web</artifactId>\n" +
                "</dependency>\n");
        boolean groupIdWasFound = false;
        boolean artifactIdWasFound = false;
        boolean dependenciesWasFound = false;
        for (int i = 0; i < data.size(); i++) {
            if (!groupIdWasFound && data.get(i).contains("{groupId}")) {
                data.set(i, data.get(i).replace("{groupId}", groupId));
                groupIdWasFound = true;
            } else if (!artifactIdWasFound && data.get(i).contains("{artifactId}")) {
                data.set(i, data.get(i).replace("{artifactId}", projectName));
                artifactIdWasFound = true;
            } else if (!dependenciesWasFound && data.get(i).contains("{dependencies}")) {
                String dependenciesString = "";
                for (String dependency : dependencies) {
                    dependenciesString += dependency;
                }
                data.set(i, data.get(i).replace("{dependencies}", dependenciesString));
                dependenciesWasFound = true;
            }
        }
        return data;
    }
}
