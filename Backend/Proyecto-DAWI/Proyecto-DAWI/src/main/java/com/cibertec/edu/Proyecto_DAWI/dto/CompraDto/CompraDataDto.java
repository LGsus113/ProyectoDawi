package com.cibertec.edu.Proyecto_DAWI.dto.CompraDto;

import java.math.BigDecimal;

public record CompraDataDto(
        Integer id_prod,
        Integer cantidad,
        BigDecimal precio_unitario
) {
}
