package com.cibertec.edu.Proyecto_DAWI.dto.CompraDto;

import java.math.BigDecimal;

public record ProductoCompraDto(
        Integer id_com,
        String nombre,
        BigDecimal precio_unitario,
        Integer cantidad,
        BigDecimal subtotal
) {
}
