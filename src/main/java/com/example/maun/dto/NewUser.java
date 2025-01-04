package com.example.maun.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUser {
    private String name;
    @NotBlank(message = "El email es obligatorio")
    @Pattern( regexp = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "El email debe ser válido" )
    private String email;
    @NotBlank(message = "El password es obligatorio")
    @Size(min = 8, max = 12, message = "El password debe tener entre 8 a 12 caracteres, no admite caracteres especiales")
    @Pattern( regexp = "^(?=.*[A-Z])(?=(?:.*\\d.*){2})(?=.*[a-z])[A-Za-z\\d]{8,12}$", message = "El password debe tener al menos una letra mayúscula, dos números y letras minúsculas" )
    private String password;
    private List<PhoneUser> phones;

}
