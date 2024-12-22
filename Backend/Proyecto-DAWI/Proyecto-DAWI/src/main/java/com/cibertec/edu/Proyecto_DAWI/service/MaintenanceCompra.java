package com.cibertec.edu.Proyecto_DAWI.service;

import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.CompraDataDto;
import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.DetalleCompraDto;
import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.ProductCompleteDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.ProductoDto;

import java.math.BigDecimal;
import java.util.List;

public interface MaintenanceCompra {
    List<DetalleCompraDto> listarComprasPorCliente(Integer idUsuario) throws Exception;

    void registrarCompra1(Integer idUsuario, String tarjeta, String detalleJson) throws Exception;

    Integer registrarCompra(Integer idUsuario, String tarjeta) throws Exception;

    List<ProductCompleteDto> listarCarrito() throws Exception;

    BigDecimal totalCarrito () throws Exception;

    Boolean agregarCarrito(List<ProductoDto> p, Integer id) throws Exception;

    Boolean removerCarrito(Integer id) throws Exception;

    Boolean actualizarCarrito(Integer id, Integer cantidad) throws Exception;
}
