package ru.tishtech.developerhelper.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import ru.tishtech.developerhelper.constants.FilePaths;

public class ReaderService {

  public List<String> readData(String fileName) {
    List<String> data = new ArrayList<>();
    try {
      BufferedReader bufferedReader =
          new BufferedReader(
              new InputStreamReader(
                  this.getClass().getResourceAsStream(FilePaths.FILES_FROM_DIR + fileName),
                  StandardCharsets.UTF_8));
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
