package com.cibertec.edu.Proyecto_DAWI.dto.CompraDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DetalleCompraDto(
        Integer id_compra,
        LocalDateTime fecha,
        BigDecimal total,
        List<ProductoCompraDto> productos
) {
}
