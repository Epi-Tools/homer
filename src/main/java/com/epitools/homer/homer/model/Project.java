package com.epitools.homer.homer.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name="project")
public class Project {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String userId;
    @NotNull
    @Column(nullable=false)
    private Integer spices;
    @NotNull
    @Column(nullable=false)
    private String name;
    @NotNull
    @Column(nullable=false)
    private String description;
    @NotNull
    @Column(nullable=false)
    private String followUp;
    @NotNull
    @Column(nullable=false)
    private String followUp1;
    @NotNull
    @Column(nullable=false)
    private String delivery;
    @NotNull
    @Column(nullable=false)
    private LocalDateTime dateFollowUp;
    @NotNull
    @Column(nullable=false)
    private LocalDateTime dateFollowUp1;
    @NotNull
    @Column(nullable=false)
    private LocalDateTime dateDelivery;
    @NotNull
    @Column(nullable=false)
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSpices() {
        return spices;
    }

    public void setSpices(Integer spices) {
        this.spices = spices;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFollowUp() {
        return followUp;
    }

    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    public String getFollowUp1() {
        return followUp1;
    }

    public void setFollowUp1(String followUp1) {
        this.followUp1 = followUp1;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public LocalDateTime getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(LocalDateTime dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public LocalDateTime getDateFollowUp1() {
        return dateFollowUp1;
    }

    public void setDateFollowUp1(LocalDateTime dateFollowUp1) {
        this.dateFollowUp1 = dateFollowUp1;
    }

    public LocalDateTime getDateFollowUp() {
        return dateFollowUp;
    }

    public void setDateFollowUp(LocalDateTime dateFollowUp) {
        this.dateFollowUp = dateFollowUp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
