package com.cibertec.edu.Proyecto_DAWI.dto.CompraDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DatosNecesariosDto(
        Integer id_compra,
        LocalDateTime fecha,
        BigDecimal total
) {
}
