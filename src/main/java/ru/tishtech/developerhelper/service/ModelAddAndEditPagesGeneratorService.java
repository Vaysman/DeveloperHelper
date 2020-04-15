package ru.tishtech.developerhelper.service;

import ru.tishtech.developerhelper.model.Variable;

import java.util.List;

public class ModelAddAndEditPagesGeneratorService {

    public static List<String> generateModelAddAndEditPagesData(List<String> data, List<Variable> variables,
                                                                String projectName, String groupId,
                                                                String capitalModel, String smallModel) {
        String variableTRs = "";
        for (Variable variable : variables) {
            String smallName = CorrectorService.toSmallString(variable.getName());
            String capitalName = CorrectorService.toCapitalString(variable.getName());
            variableTRs += "\t\t<tr>\n" +
                    "\t\t\t<th>" + capitalName + "</th>\n" +
                    "\t\t\t<td><input type=\"text\" th:value=\"${" + smallModel +
                    "." + smallName + "}\" name=\"" + smallName + "\"></td>\n" +
                    "\t\t</tr>\n";
        }
        boolean groupIdWasFound = false;
        boolean projectNameWasFound = false;
        boolean variableTRsWereFound = false;
        for (int i = 0; i < data.size(); i++) {
            if (!groupIdWasFound && data.get(i).contains("{groupId}")) {
                data.set(i, data.get(i).replace("{groupId}", groupId));
                groupIdWasFound = true;
            }
            if (!projectNameWasFound && data.get(i).contains("{projectName}")) {
                data.set(i, data.get(i).replace("{projectName}", projectName));
                projectNameWasFound = true;
            }
            if (data.get(i).contains("{Model}"))
                data.set(i, data.get(i).replace("{Model}", capitalModel));
            if (data.get(i).contains("{model}"))
                data.set(i, data.get(i).replaceAll("\\{model}", smallModel));
            if (!variableTRsWereFound && data.get(i).contains("{variableTRs}")) {
                data.set(i, data.get(i).replace("{variableTRs}", variableTRs));
                variableTRsWereFound = true;
            }
        }
        return data;
    }
}
