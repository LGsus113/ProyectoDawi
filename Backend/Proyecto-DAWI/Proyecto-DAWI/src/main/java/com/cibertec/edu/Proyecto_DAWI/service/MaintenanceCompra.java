package com.cibertec.edu.Proyecto_DAWI.service;

import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.CompraDataDto;

import java.util.List;

public interface MaintenanceCompra {
    void registrarCompra1(Integer idUsuario, String tarjeta, String detalleJson) throws Exception;

    Integer registrarCompra(Integer idUsuario, String tarjeta, List<CompraDataDto> detalle) throws Exception;
}
