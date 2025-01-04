package com.example.maun.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



public class ErrorList {
    private List<ErrorResponse> error;

    public ErrorList() {
        this.error = new ArrayList<>();
    }

    public List<ErrorResponse> getError() {
        return error;
    }
    public void setErrores(List<ErrorResponse> errores) {
        this.error=errores;
    }
    public void setError(ErrorResponse error) {
        this.error.add(error);
    }
}
