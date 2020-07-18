package ru.tishtech.developerhelper.service.generator;

import java.util.List;

public class ControllerGeneratorService {

  public static List<String> generateControllerData(
      List<String> data,
      String projectName,
      String groupId,
      String capitalModel,
      String smallModel) {
    for (int i = 0; i < data.size(); i++) {
      if (data.get(i).contains("{groupId}")) data.set(i, data.get(i).replace("{groupId}", groupId));
      if (data.get(i).contains("{projectName}"))
        data.set(i, data.get(i).replace("{projectName}", projectName));
      if (data.get(i).contains("{Model}"))
        data.set(i, data.get(i).replace("{Model}", capitalModel));
      if (data.get(i).contains("{model}"))
        data.set(i, data.get(i).replaceAll("\\{model}", smallModel));
    }
    return data;
  }
}
