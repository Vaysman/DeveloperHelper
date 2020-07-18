package ru.tishtech.developerhelper.service;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class WriterService {

  public static void writeData(List<String> data, String fileName, String folder) {
    File filePath = new File(folder);
    if (!filePath.exists()) {
      filePath.mkdirs();
    }
    try {
      BufferedWriter bufferedWriter =
          new BufferedWriter(new FileWriter(new File(filePath, fileName)));
      for (String line : data) {
        bufferedWriter.write(line + "\n");
      }
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
