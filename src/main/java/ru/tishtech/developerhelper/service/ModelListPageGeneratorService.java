package ru.tishtech.developerhelper.service;

import ru.tishtech.developerhelper.model.Variable;

import java.util.List;

public class ModelListPageGeneratorService {

    public static List<String> generateModelListPageData(List<String> data, List<Variable> variables, String model) {
        String variableTHeads = "";
        String variableTDs = "";
        for (Variable variable : variables) {
            variableTHeads += "\t\t<th>" + variable.getName() + "</th>\n";
            variableTDs += "\t\t<td th:text=\"${" + model.toLowerCase() + "." + variable.getName() + "}\"></td>\n";
        }
        boolean variableTHeadsWereFound = false;
        boolean variableTDsWereFound = false;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).contains("{Model}"))
                data.set(i, data.get(i).replace("{Model}", model));
            else if (data.get(i).contains("{model}"))
                data.set(i, data.get(i).replaceAll("\\{model}", model.toLowerCase()));
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
