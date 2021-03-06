package com.epitools.homer.homer.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="contributor")
public class Contributor {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Column(nullable=false)
    private Integer userId;
    @NotNull
    @Column(nullable=false)
    private Integer projectId;

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
}
