package com.cibertec.edu.Proyecto_DAWI.TemplatesController;

import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.CompraDataDto;
import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.CompraRequestDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.CreateProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.ProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.StockProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.UpdateDetailProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.UsuarioDto;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceProducto;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/start")
public class ProductoTController {
    @Autowired
    MaintenanceProducto maintenanceProducto;

    @Autowired
    MaintenanceUsuario maintenanceUsuario;

    @GetMapping("/login")
    public String login(Model model) {
        return "Login";
    }

    @GetMapping("/restricted")
    public String restricted(Model model) {
        return "Restricted";
    }

    @GetMapping("/products-all")
    public String start(
            Model model,
            @RequestParam(defaultValue = "true") Boolean listado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<ProductoDto> productosPage = maintenanceProducto.productosPorDisponibilidadPaginado(listado, page, size);
            model.addAttribute("productosPage", productosPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productosPage.getTotalPages());
            model.addAttribute("error", null);
        } catch (Exception e) {
            model.addAttribute("error", "Error al traer los datos: " + e.getMessage());
        }

        return "Products";
    }

    @GetMapping("/products-fragment")
    public String getProductsFragment(
            Model model,
            @RequestParam(defaultValue = "true") Boolean listado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<ProductoDto> productosPage = maintenanceProducto.productosPorDisponibilidadPaginado(listado, page, size);
            model.addAttribute("productosPage", productosPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productosPage.getTotalPages());
            model.addAttribute("error", null);
        } catch (Exception e) {
            model.addAttribute("error", "Error al traer los datos: " + e.getMessage());
        }

        return "fragments/productos :: productosTable"; //acá devolveremos solo un pedazo de hmtl, nos servirá para modificar la tabla de productos
    }

    @GetMapping("/home")
    public String home(Model model) {
        try {
            List<ProductoDto> productos = maintenanceProducto.productosPorDisponibilidad(true);
            model.addAttribute("productos", productos);
            model.addAttribute("error", null);
        } catch (Exception e) {
            model.addAttribute("error", "Error al traer los datos: " + e.getMessage());
        }

        return "Home";
    }

    @GetMapping("/car-to-shop")
    public String car(Model model, Principal principal) {
        try {
            UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
            String username = userDetails.getUsername();

            UsuarioDto usuario = maintenanceUsuario.usuario(username);

            if (usuario == null) {
                System.out.println("Usuario no encontrado para el email: " + username);
                return "redirect:/start/home";
            }

            Integer id = usuario.idUsuario();
            model.addAttribute("idUsuario", id);
            return "Car";
        } catch (Exception e) {
            System.out.println("Error en el metodo: " + e.getMessage());
            return "redirect:/start/home";
        }
    }

    @PostMapping("/procesar-carrito")
    @ResponseBody
    public ResponseEntity<String> procesarCarrito(@RequestBody CompraRequestDto compraRequest) {
        try {
            Integer idUsuario = compraRequest.idUsuario();
            String tarjeta = compraRequest.tarjeta();
            List<CompraDataDto> detalleJson = compraRequest.detalleJson();

            System.out.println("Compra recibida: " + compraRequest);
            return ResponseEntity.ok("Compra procesada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al procesar la compra");
        }
    }

    @GetMapping("/add")
    public String add() {
        return "Nuevo-Producto";
    }

    @PostMapping("/add-new")
    public String addNew(@ModelAttribute CreateProductoDto createProducto) {
        try {
            maintenanceProducto.createProducto(createProducto);
            return "redirect:/start/products-all";
        } catch (Exception e) {
            return "Nuevo-Producto";
        }
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Integer idProduct, @RequestParam Boolean estado, Model model) {
        try {
            List<ProductoDto> productos = maintenanceProducto.productosPorDisponibilidad(estado);
            ProductoDto productoDto = productos.stream().filter(p -> p.idProducto().equals(idProduct)).findFirst().orElse(null);

            model.addAttribute("producto", productoDto);
            model.addAttribute("error", null);
        } catch (Exception e) {
            model.addAttribute("error", "Error al traer los datos: " + e.getMessage());
        }

        return "Actualizar-Producto";
    }

    @PutMapping("/especification-new")
    public String actualizarProducto(@ModelAttribute UpdateDetailProductoDto updateProducto, Model model) {
        try {
            Boolean updated = maintenanceProducto.updateProducto(updateProducto);

            if (Boolean.TRUE.equals(updated)) {
                return "redirect:/start/products-all";
            }

            model.addAttribute("error", "No se pudo actualizar el producto.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el producto: " + e.getMessage());
        }

        return "ActualizarProducto";
    }

    @PutMapping("/update-stock")
    public String cambiarStock(@ModelAttribute StockProductoDto stockProductoDto, Model model) {
        try {
            Boolean updated = maintenanceProducto.updateStockProducto(stockProductoDto);

            if (Boolean.FALSE.equals(updated)) {
                model.addAttribute("error", "No se pudo actualizar el stock.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el stock: " + e.getMessage());
        }

        return "redirect:/start/products-all";
    }

    @PutMapping("/delete/{id}")
    public String disponibilidadProducto(@PathVariable("id") Integer id, @RequestParam Integer opcion, RedirectAttributes redirectAttributes) {
        Boolean respuesta = null;
        String tipoOperacion = "";

        try {
            if (opcion == 1) {
                respuesta = maintenanceProducto.habilitarProductos(id);
                tipoOperacion = "habilitado";
            } else if (opcion == 2) {
                respuesta = maintenanceProducto.deshabilitarProductos(id);
                tipoOperacion = "deshabilitado";
            } else {
                redirectAttributes.addFlashAttribute("error", "Opción no disponible.");
            }

            String respuesta1 = "Producto " + tipoOperacion + " con éxito.";
            String respuesta2 = "El producto ya está " + tipoOperacion + ".";

            if (Boolean.TRUE.equals(respuesta)) {
                redirectAttributes.addFlashAttribute("success", respuesta1);
            } else {
                redirectAttributes.addFlashAttribute("error", respuesta2);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un error: " + e.getMessage());
        }

        return "redirect:/start/products-all";
    }
}
