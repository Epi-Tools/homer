package com.epitools.homer.homer.model;
import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @Column(nullable=false)
    private String email;
    @Column(columnDefinition="boolean default false", nullable=false)
    private boolean admin;
    @Column(columnDefinition="integer default 60", nullable=false)
    private Integer spices;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Integer getSpices() {
        return spices;
    }

    public void setSpices(Integer spices) {
        this.spices = spices;
    }
}
