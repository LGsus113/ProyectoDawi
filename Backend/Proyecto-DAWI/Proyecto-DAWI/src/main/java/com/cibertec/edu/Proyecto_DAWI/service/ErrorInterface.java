package com.cibertec.edu.Proyecto_DAWI.service;

import java.util.List;
import java.util.function.Function;

public interface ErrorInterface {
    <T> List<T> generarErrorDto(Exception e, Function<String, T> errorDtoCreator);
}
