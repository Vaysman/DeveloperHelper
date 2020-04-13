package ru.tishtech.developerhelper.service;

import ru.tishtech.developerhelper.constants.FilePaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ReaderService {

    public static List<String> readData(String fileName) {
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
