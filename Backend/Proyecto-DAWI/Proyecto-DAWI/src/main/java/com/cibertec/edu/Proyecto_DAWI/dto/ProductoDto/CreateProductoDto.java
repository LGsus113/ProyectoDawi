package com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto;

import java.math.BigDecimal;

public record CreateProductoDto(
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock
) {
}
