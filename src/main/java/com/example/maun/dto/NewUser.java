package com.example.maun.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUser {
    private String name;
    private String email;
    private String password;
    private List<PhoneUser> phones;

}
