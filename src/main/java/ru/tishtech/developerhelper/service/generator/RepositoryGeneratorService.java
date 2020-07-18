package ru.tishtech.developerhelper.service.generator;

import java.util.List;

public class RepositoryGeneratorService {

  public static List<String> generateRepositoryData(
      List<String> data, String projectName, String groupId, String capitalModel) {
    for (int i = 0; i < data.size(); i++) {
      if (data.get(i).contains("{groupId}")) data.set(i, data.get(i).replace("{groupId}", groupId));
      if (data.get(i).contains("{projectName}"))
        data.set(i, data.get(i).replace("{projectName}", projectName));
      if (data.get(i).contains("{Model}"))
        data.set(i, data.get(i).replaceAll("\\{Model}", capitalModel));
    }
    return data;
  }
}
