package com.example.maun.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;


import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table(name = "\"user\"")
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @NotNull
        private String name;

        @NotNull
        @Column(unique = true)
        @Email(message = "El correo ingresado no es valido")
        private String email;

        @NotNull
        @Pattern(regexp = "[a-zA-Z0-9]+", message = "La clave solo debe contener letras y numeros")
        private String password;

        @CreatedDate
        private LocalDateTime created;

        @LastModifiedDate
        private LocalDateTime modified;

        @Column(columnDefinition = "boolean default true")
        private Boolean isactive;

        private String usertoken;

        @OneToMany(mappedBy = "user", targetEntity=Phone.class)
        private  List<Phone> phones=new ArrayList<Phone>();

    public User(UUID id,  String name,  String email,  String password, LocalDateTime created,
                LocalDateTime modified, Boolean isactive, String usertoken, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = created;
        this.modified = modified;
        this.isactive = isactive;
        this.usertoken= usertoken;
        this.phones = phones;
    }

    public User(  String name,  String email,  String password, String usertoken,List<Phone> phones) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
        this.isactive = true;
        this.usertoken= usertoken;
        this.phones = phones;
    }

    public User() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }
    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public String getUsertoken() {
        return usertoken;
    }

    public void setUsertoken(String usertoken) {
        this.usertoken = usertoken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", isactive=" + isactive +
                ", phones=" + phones +
                '}';
    }
}
