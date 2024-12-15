package com.cibertec.edu.Proyecto_DAWI.TemplatesController;

import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.ProductoDto;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/start")
public class ProductoTController {
    @Autowired
    MaintenanceProducto maintenanceProducto;

    @GetMapping("/login")
    public String login(Model model) { return "Login"; }

    @GetMapping("/restricted")
    public String restricted(Model model) { return "Restricted"; };

    @GetMapping("/products-all")
    public String start(Model model, @RequestParam(defaultValue = "true") Boolean listado) {
        try {
            List<ProductoDto> productos = maintenanceProducto.productosPorDisponibilidad(listado);
            model.addAttribute("productos", productos);
            model.addAttribute("error", null);
        } catch (Exception e) {
            model.addAttribute("error", "Error al traer los datos: " + e.getMessage());
        }

        return "Products";
    }

    @GetMapping("/products-fragment")
    public String getProductsFragment(@RequestParam(defaultValue = "true") Boolean listado, Model model) {
        try {
            List<ProductoDto> productos = maintenanceProducto.productosPorDisponibilidad(listado);
            model.addAttribute("productos", productos);
            model.addAttribute("error", null);
        } catch (Exception e) {
            model.addAttribute("error", "Error al traer los datos: " + e.getMessage());
        }

        return "fragments/productos :: productosTable"; //acá devolveremos solo un pedazo de hmtl, nos servirá para modificar la tabla de productos
    }
}
