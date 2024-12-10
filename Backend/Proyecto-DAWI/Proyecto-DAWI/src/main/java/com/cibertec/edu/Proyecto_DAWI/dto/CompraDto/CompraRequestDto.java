package com.cibertec.edu.Proyecto_DAWI.dto.CompraDto;

import java.util.List;

public record CompraRequestDto(
        Integer idUsuario,
        String tarjeta,
        List<CompraDataDto> detalleJson
) {
}
