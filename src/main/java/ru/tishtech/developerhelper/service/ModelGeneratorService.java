package ru.tishtech.developerhelper.service;

import ru.tishtech.developerhelper.model.Variable;

import java.util.ArrayList;
import java.util.List;

public class ModelGeneratorService {

    public static List<String> generateModelData(List<String> data, List<Variable> variables,
                                         String projectName, String groupId, String model) {
        data.set(0, data.get(0).replace("{groupId}", groupId));
        data.set(0, data.get(0).replace("{projectName}", projectName));
        String variablesString = "";
        List<String> getters = new ArrayList<>();
        List<String> setters = new ArrayList<>();
        for (Variable variable : variables) {
            variablesString += "\tprivate " + variable.getType() + " " + variable.getName() + ";\n";
            String capitalVariable = variable.getName().substring(0, 1).toUpperCase() +
                    variable.getName().substring(1);
            getters.add("\tpublic " + variable.getType() + " get" + capitalVariable + "() {\n" +
                    "\t\treturn " + variable.getName() + ";\n" + "\t}\n");
            setters.add("\tpublic void set" + capitalVariable + "(" + variable.getType() + " " + variable.getName() + ") {\n" +
                    "\t\tthis." + variable.getName() + " = " + variable.getName() + ";\n" + "\t}\n");
        }
        String gettersAndSettersString = "";
        for (int i = 0; i < getters.size(); i++) {
            gettersAndSettersString += getters.get(i) + "\n" + setters.get(i) + "\n";
        }
        boolean modelWasFound = false;
        boolean variablesWereFound = false;
        boolean gettersAndSettersWereFound = false;
        for (int i = 1; i < data.size(); i++) {
            if (!modelWasFound && data.get(i).contains("{Model}")) {
                data.set(i, data.get(i).replace("{Model}", model));
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
