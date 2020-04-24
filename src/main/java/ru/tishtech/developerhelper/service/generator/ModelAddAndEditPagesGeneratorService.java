package ru.tishtech.developerhelper.service.generator;

import ru.tishtech.developerhelper.model.Variable;
import ru.tishtech.developerhelper.service.CorrectorService;

import java.util.List;

public class ModelAddAndEditPagesGeneratorService {

    public static List<String> generateModelAddAndEditPagesData(List<String> data, List<Variable> variables,
                                                                String capitalModel, String smallModel) {
        String variableTRs = "";
        for (Variable variable : variables) {
            String smallName = CorrectorService.toSmallString(variable.getName());
            String capitalName = CorrectorService.toCapitalString(variable.getName());
            variableTRs += "\t\t<tr>\n" +
                    "\t\t\t<th>" + capitalName + "</th>\n" +
                    "\t\t\t<td><input type=\"text\" th:field=\"*{" + smallName + "}\"></td>\n" +
                    "\t\t</tr>\n";
        }
        boolean variableTRsWereFound = false;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).contains("{Model}"))
                data.set(i, data.get(i).replace("{Model}", capitalModel));
            else if (data.get(i).contains("{model}"))
                data.set(i, data.get(i).replaceAll("\\{model}", smallModel));
            else if (!variableTRsWereFound && data.get(i).contains("{variableTRs}")) {
                data.set(i, data.get(i).replace("{variableTRs}", variableTRs));
                variableTRsWereFound = true;
            }
        }
        return data;
    }
}
