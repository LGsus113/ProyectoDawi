package com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto;

import java.math.BigDecimal;

public record ProductoDto(
        Integer idProducto,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        Boolean estado
) {
}
