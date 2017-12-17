package com.epitools.homer.homer.model;

import javax.validation.constraints.NotNull;

// TODO add spices value validation
public class BetValidation {

    @NotNull
    private Integer id;

    @NotNull
    private Integer spices;

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
}