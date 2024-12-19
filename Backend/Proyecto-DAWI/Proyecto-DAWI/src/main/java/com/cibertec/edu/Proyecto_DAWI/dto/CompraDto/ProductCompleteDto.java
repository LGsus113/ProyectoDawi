package com.cibertec.edu.Proyecto_DAWI.dto.CompraDto;

import java.math.BigDecimal;

public record ProductCompleteDto(
        Integer idProducto,
        String nombre,
        BigDecimal precio,
        Integer cantidad,
        Integer stock,
        BigDecimal totalCoste
) {
}
