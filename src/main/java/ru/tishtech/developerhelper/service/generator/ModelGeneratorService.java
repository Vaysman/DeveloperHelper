package ru.tishtech.developerhelper.service.generator;

import java.util.List;
import ru.tishtech.developerhelper.model.Variable;
import ru.tishtech.developerhelper.service.CorrectorService;

// FIXME: switch to any templating engine
public class ModelGeneratorService {

  public static List<String> generateModelData(
      List<String> data,
      List<Variable> variables,
      String projectName,
      String groupId,
      String capitalModel) {
    data.set(0, data.get(0).replace("{groupId}", groupId));
    data.set(0, data.get(0).replace("{projectName}", projectName));
    String variablesString = "";
    String gettersAndSettersString = "";
    for (Variable variable : variables) {
      String smallName = CorrectorService.toSmallString(variable.getName());
      String capitalName = CorrectorService.toCapitalString(variable.getName());
      // FIXME: use StringBuilder
      variablesString += "\tprivate " + variable.getType() + " " + smallName + ";\n";
      // FIXME: use StringBuilder
      gettersAndSettersString +=
          "\tpublic "
              + variable.getType()
              + " get"
              + capitalName
              + "() {\n"
              + "\t\treturn "
              + smallName
              + ";\n"
              + "\t}\n\n";
      gettersAndSettersString +=
          "\tpublic void set"
              + capitalName
              + "("
              + variable.getType()
              + " "
              + smallName
              + ") {\n"
              + "\t\tthis."
              + smallName
              + " = "
              + smallName
              + ";\n"
              + "\t}\n\n";
    }
    boolean modelWasFound = false;
    boolean variablesWereFound = false;
    boolean gettersAndSettersWereFound = false;
    for (int i = 1; i < data.size(); i++) {
      if (!modelWasFound && data.get(i).contains("{Model}")) {
        data.set(i, data.get(i).replace("{Model}", capitalModel));
        modelWasFound = true;
      } else if (!variablesWereFound && data.get(i).contains("{variables}")) {
        data.set(i, data.get(i).replace("{variables}", variablesString));
        variablesWereFound = true;
      } else if (!gettersAndSettersWereFound && data.get(i).contains("{GettersAndSetters}")) {
        data.set(i, data.get(i).replace("{GettersAndSetters}", gettersAndSettersString));
        gettersAndSettersWereFound = true;
      }
    }
    return data;
  }
}
