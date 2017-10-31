package com.epitools.homer.homer.model;

import javax.persistence.*;
import java.time.LocalDateTime;

public class Project {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private String userId;
    @Column(columnDefinition="boolean default false", nullable=false)
    private Integer spices;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String description;
    @Column(nullable=false)
    private String followUp;
    @Column(nullable=false)
    private String followUp1;
    @Column(nullable=false)
    private String delivery;
    @Column(nullable=false)
    private LocalDateTime dateFollowUp1;
    @Column(nullable=false)
    private LocalDateTime dateFollowUp2;
    @Column(nullable=false)
    private LocalDateTime dateDelivery;

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

    public LocalDateTime getDateFollowUp2() {
        return dateFollowUp2;
    }

    public void setDateFollowUp2(LocalDateTime dateFollowUp2) {
        this.dateFollowUp2 = dateFollowUp2;
    }

    public LocalDateTime getDateFollowUp1() {
        return dateFollowUp1;
    }

    public void setDateFollowUp1(LocalDateTime dateFollowUp1) {
        this.dateFollowUp1 = dateFollowUp1;
    }
}
