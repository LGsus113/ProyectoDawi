package com.cibertec.edu.Proyecto_DAWI.service;

import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto;

import java.util.List;

public interface MaintenanceProducto {
    List<ProductoDto> productosPorDisponibilidad(boolean disponibilidad);
}
