package ru.tishtech.developerhelper.model;

import java.util.ArrayList;
import java.util.List;

public class VariableStore {

    private List<Variable> variables = new ArrayList<>();

    public List<Variable> getVariables() {
        return variables;
    }

    public void setVariables(List<Variable> variables) {
        this.variables = variables;
    }
}
