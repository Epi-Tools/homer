package com.epitools.homer.homer.model;

import javax.validation.constraints.NotNull;

public class BetProjectProvider extends BetProvider {

    @NotNull
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
