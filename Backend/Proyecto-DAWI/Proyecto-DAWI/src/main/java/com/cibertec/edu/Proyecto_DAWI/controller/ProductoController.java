package com.cibertec.edu.Proyecto_DAWI.controller;

import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto;
import com.cibertec.edu.Proyecto_DAWI.service.ErrorInterface;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    MaintenanceProducto maintenanceProducto;

    @Autowired
    ErrorInterface errorInterface;

    @GetMapping("/disponibles")
    public List<ProductoDto> listaProductos() {
        try {
            return maintenanceProducto.productosPorDisponibilidad(true);
        } catch (Exception e) {
            return errorInterface.generarErrorDto(e, descripcion -> new ProductoDto(
                    -1,
                    "Error",
                    descripcion,
                    BigDecimal.ZERO,
                    0,
                    false
            ));
        }
    }

    @GetMapping("/no-disponibles")
    public List<ProductoDto> listaProductosNoDisponibles() {
        try {
            List<ProductoDto> productos = maintenanceProducto.productosPorDisponibilidad(false);

            if (productos.isEmpty()) {
                ProductoDto productoVacio = new ProductoDto(
                        -2,
                        "No hay productos no disponibles",
                        "Actualmente todos los productos etan disponibles",
                        BigDecimal.ZERO,
                        0,
                        false
                );

                productos.add(productoVacio);
            }

            return productos;
        } catch (Exception e) {
            return errorInterface.generarErrorDto(e, descripcion -> new ProductoDto(
                    -1,
                    "Error",
                    descripcion,
                    BigDecimal.ZERO,
                    0,
                    false
            ));
        }
    }
}
