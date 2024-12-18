package com.cibertec.edu.Proyecto_DAWI.controller;

import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.CreateProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.ProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.StockProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.UpdateDetailProductoDto;
import com.cibertec.edu.Proyecto_DAWI.service.ErrorInterface;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/crear")
    public String crearProducto(@RequestBody CreateProductoDto createProducto) {
        try {
            maintenanceProducto.createProducto(createProducto);
            return "Producto creado con éxito";
        } catch (Exception e) {
            return "Hubo error al crear: " + e.getMessage();
        }
    }

    @PutMapping("/disponibilidad")
    public String disponibilidadProducto(@RequestParam Integer idProd, @RequestParam Integer opcion) {
        try {
            Boolean respuesta = null;
            String tipoOperacion = "";

            if (opcion == 1) {
                respuesta = maintenanceProducto.habilitarProductos(idProd);
                tipoOperacion = "habilitado";
            } else if (opcion == 2) {
                respuesta = maintenanceProducto.deshabilitarProductos(idProd);
                tipoOperacion = "deshabilitado";
            } else {
                return "Opcion no disponible";
            }

            return respuesta ? tipoOperacion + " con exito" : "Ya esta " + tipoOperacion;
        } catch (Exception e) {
            return "hubo un error en el procedimiento, el error es: " + e.getMessage();
        }
    }

    @PutMapping("/actualizar")
    public String actualizarProducto(@RequestBody UpdateDetailProductoDto updateProducto) {
        try {
            Boolean respuesta = maintenanceProducto.updateProducto(updateProducto);
            return  respuesta ? "Producto Actualizado" : "No se puedo actualizar";
        } catch (Exception e) {
            return "Hubo un error, es el siguiente: " + e.getMessage();
        }
    }

    @PutMapping("/mas-stock")
    public String aumentarStock(@RequestBody StockProductoDto stockProductoDto) {
        try {
            Boolean respuesta = maintenanceProducto.updateStockProducto(stockProductoDto);
            return  respuesta ? "Stock actualizado" : "No se puedo actualizar stock";
        } catch (Exception e) {
            return "Hubo un error, es el siguiente: " + e.getMessage();
        }
    }


}
