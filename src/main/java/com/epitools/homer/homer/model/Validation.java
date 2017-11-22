package com.epitools.homer.homer.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="validation")
public class Validation {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Column(nullable=false)
    private String userId;
    @Column(nullable=false)
    private String projectId;
    @Column(nullable=false)
    private Integer projectStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Integer projectStatus) {
        this.projectStatus = projectStatus;
    }
}
