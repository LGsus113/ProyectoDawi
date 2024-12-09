package com.cibertec.edu.Proyecto_DAWI.service.impl;

import com.cibertec.edu.Proyecto_DAWI.service.ErrorInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class ErrorImpl implements ErrorInterface {
    @Override
    public <T> List<T> generarErrorDto(Exception e, Function<String, T> errorDtoCreator) {
        String descripcionError = "Error en la operación: " + e.getMessage();
        T errorDto = errorDtoCreator.apply(descripcionError);
        return List.of(errorDto);
    }
}
