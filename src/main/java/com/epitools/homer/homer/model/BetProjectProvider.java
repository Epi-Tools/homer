package com.epitools.homer.homer.model;

import javax.validation.constraints.NotNull;

public class BetProjectProvider extends BetProvider {

    @NotNull
    private String projectName;

    @NotNull
    private Integer projectStatus;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }
}
