package com.epitools.homer.homer.model;

import javax.persistence.*;

@Entity
public class Bet {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String userId;
    @Column(columnDefinition="integer default 5")
    private Integer spices;

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
