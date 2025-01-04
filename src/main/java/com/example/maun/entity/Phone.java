package com.example.maun.entity;

import com.example.maun.util.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "phone")
public class Phone {
    @Id
    private UUID id;

    @NotNull(message = "El número de teléfono es requerido")
    private Long number;

    @NotNull(message = "El código de ciudad es requerido")
    private Integer citycode;

    @NotNull(message = "El código de país es requerido")
    private String countrycode;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user=new User();

    public Phone(UUID id, Long number, Integer citycode, String countrycode) {
        this.id = Utils.generateUUID(id);
        this.number = number;
        this.citycode = citycode;
        this.countrycode = countrycode;
    }

    public Phone(UUID id, Long number, Integer citycode, String countrycode, User user) {
        this.id = Utils.generateUUID(id);
        this.number = number;
        this.citycode = citycode;
        this.countrycode = countrycode;
        this.user = user;
    }

    public Phone() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Integer getCitycode() {
        return citycode;
    }

    public void setCitycode(Integer citycode) {
        this.citycode = citycode;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", citycode='" + citycode + '\'' +
                ", countrycode='" + countrycode + '\'' +
                ", user=" + user +
                '}';
    }
}
