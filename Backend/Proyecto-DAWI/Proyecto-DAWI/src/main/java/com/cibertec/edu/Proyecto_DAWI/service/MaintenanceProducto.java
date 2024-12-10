package com.cibertec.edu.Proyecto_DAWI.service;

import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.CreateProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.ProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.StockProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.UpdateDetailProductoDto;

import java.util.List;

public interface MaintenanceProducto {
    List<ProductoDto> productosPorDisponibilidad(boolean disponibilidad);

    void createProducto(CreateProductoDto createProducto);

    Boolean updateProducto(UpdateDetailProductoDto updateProducto);

    Boolean updateStockProducto(StockProductoDto stockProductoDto);

    Boolean habilitarProductos(Integer idProducto);

    Boolean deshabilitarProductos(Integer idProducto);
}
