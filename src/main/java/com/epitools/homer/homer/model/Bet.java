package com.epitools.homer.homer.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="bet")
public class Bet {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Column(nullable=false)
    private String userId;
    @Column(nullable=false)
    private Integer spices = 5;

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

    public Integer getSpices() {
        return spices;
    }

    public void setSpices(Integer spices) {
        this.spices = spices;
    }
}
