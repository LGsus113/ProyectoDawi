package com.cibertec.edu.Proyecto_DAWI.service;

import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.CreateProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.ProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.StockProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.UpdateDetailProductoDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MaintenanceProducto {
    List<ProductoDto> productosPorDisponibilidad(boolean disponibilidad) throws Exception;

    Page<ProductoDto> productosPorDisponibilidadPaginado(boolean disponibilidad, int page, int size) throws Exception;

    void createProducto(CreateProductoDto createProducto) throws Exception;

    Boolean updateProducto(UpdateDetailProductoDto updateProducto) throws Exception;

    Boolean updateStockProducto(StockProductoDto stockProductoDto) throws Exception;

    Boolean habilitarProductos(Integer idProducto) throws Exception;

    Boolean deshabilitarProductos(Integer idProducto) throws Exception;
}
