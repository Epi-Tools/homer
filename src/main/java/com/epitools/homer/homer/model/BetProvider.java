package com.epitools.homer.homer.model;

import javax.validation.constraints.NotNull;

public class BetProvider {

    @NotNull
    protected Integer id;

    @NotNull
    protected Integer userId;

    @NotNull
    protected Integer projectId;

    @NotNull
    protected String username;

    @NotNull
    protected Integer spices;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSpices() {
        return spices;
    }

    public void setSpices(Integer spices) {
        this.spices = spices;
    }
}
