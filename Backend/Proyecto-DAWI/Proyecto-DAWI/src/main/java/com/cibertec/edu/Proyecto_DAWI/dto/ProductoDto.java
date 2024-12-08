package com.cibertec.edu.Proyecto_DAWI.dto;

public record ProductoDto(
        Integer idProducto,
        String nombre,
        String descripcion,
        Double precio,
        Integer stock
) {
}
