package ru.tishtech.developerhelper.service.generator;

import java.util.List;
import ru.tishtech.developerhelper.model.Variable;
import ru.tishtech.developerhelper.service.CorrectorService;

public class ModelListPageGeneratorService {

  public static List<String> generateModelListPageData(
      List<String> data, List<Variable> variables, String capitalModel, String smallModel) {
    String variableTHeads = "";
    String variableTDs = "";
    for (Variable variable : variables) {
      String smallName = CorrectorService.toSmallString(variable.getName());
      String capitalName = CorrectorService.toCapitalString(variable.getName());
      variableTHeads += "\t\t<th>" + capitalName + "</th>\n";
      variableTDs += "\t\t<td th:text=\"${" + smallModel + "." + smallName + "}\"></td>\n";
    }
    boolean variableTHeadsWereFound = false;
    boolean variableTDsWereFound = false;
    for (int i = 0; i < data.size(); i++) {
      if (data.get(i).contains("{Model}"))
        data.set(i, data.get(i).replace("{Model}", capitalModel));
      else if (data.get(i).contains("{model}"))
        data.set(i, data.get(i).replaceAll("\\{model}", smallModel));
      else if (!variableTHeadsWereFound && data.get(i).contains("{variableTHeads}")) {
        data.set(i, data.get(i).replace("{variableTHeads}", variableTHeads));
        variableTHeadsWereFound = true;
      } else if (!variableTDsWereFound && data.get(i).contains("{variableTDs}")) {
        data.set(i, data.get(i).replace("{variableTDs}", variableTDs));
        variableTDsWereFound = true;
      }
    }
    return data;
  }
}
