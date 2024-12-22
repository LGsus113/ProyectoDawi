package com.cibertec.edu.Proyecto_DAWI.TemplatesController;

import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.DetalleCompraDto;
import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.ProductCompleteDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.CreateProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.ProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.StockProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.UpdateDetailProductoDto;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceCompra;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceProducto;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/start")
public class ProductoTController {
    @Autowired
    MaintenanceProducto maintenanceProducto;

    @Autowired
    MaintenanceUsuario maintenanceUsuario;

    @Autowired
    MaintenanceCompra maintenanceCompra;

    @GetMapping("/login")
    public String login(
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "error", required = false) String error,
            Model model
    ) {
        if (logout != null) {
            model.addAttribute("logout", true);
        }

        if (error != null) {
            model.addAttribute("error", true);
        }
        return "Login";
    }

    @GetMapping("/restricted")
    public String restricted() {
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
            String textTitle = listado ? "Productos disponibles" : "Productos no disponibles";

            Page<ProductoDto> productosPage = maintenanceProducto.productosPorDisponibilidadPaginado(listado, page, size);
            model.addAttribute("productosPage", productosPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productosPage.getTotalPages());
            model.addAttribute("title", textTitle);
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
            String textTitle = listado ? "Productos disponibles" : "Productos no disponibles";

            Page<ProductoDto> productosPage = maintenanceProducto.productosPorDisponibilidadPaginado(listado, page, size);
            model.addAttribute("productosPage", productosPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productosPage.getTotalPages());
            model.addAttribute("title", textTitle);
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

    @GetMapping("/env-cart/{id}")
    public String enviar(
            @PathVariable("id") Integer idProduct,
            RedirectAttributes redirectAttributes
    ) {
        try {
            List<ProductoDto> productos = maintenanceProducto.productosPorDisponibilidad(true);
            Boolean respuesta = maintenanceCompra.agregarCarrito(productos, idProduct);

            if (respuesta) {
                redirectAttributes.addFlashAttribute(
                        "message",
                        "Producto enviado"
                );
            } else {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Fallo en el proceso de envio"
                );
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Excepcion: " + e.getMessage()
            );
        }

        return "redirect:/start/home";
    }

    @GetMapping("/del-cart/{id}")
    public String quitar(
            @PathVariable("id") Integer idProduct,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Boolean resultado = maintenanceCompra.removerCarrito(idProduct);

            if (resultado) {
                redirectAttributes.addFlashAttribute(
                        "message",
                        "Producto eliminado del carrito"
                );
            } else {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Producto no removido por alguna inconsistencia"
                );
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Excepción: " + e.getMessage()
            );
        }

        return "redirect:/start/car-to-shop";
    }

    @GetMapping("/car-to-shop")
    public String car(
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer id = maintenanceUsuario.idUsuario(principal);
            if (id == -1) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/start/home";
            }

            List<ProductCompleteDto> productos = maintenanceCompra.listarCarrito();
            BigDecimal totalCarrito = maintenanceCompra.totalCarrito();

            model.addAttribute("idUsuario", id);
            model.addAttribute("productoTable", productos);
            model.addAttribute("totalCarrito", totalCarrito);

            return "Car";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error en el metodo: " + e.getMessage());
            return "redirect:/start/home";
        }
    }

    @GetMapping("/update-cart/{id}")
    public String updateCart(
            @PathVariable Integer id,
            @RequestParam Integer cantidad,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Boolean resultado = maintenanceCompra.actualizarCarrito(id, cantidad);

            if (resultado) {
                redirectAttributes.addFlashAttribute(
                        "message",
                        "Producto actualizado en el carrito"
                );
            } else {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Producto no actualizado por alguna inconsistencia"
                );
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Excepción: " + e.getMessage()
            );
        }

        return "redirect:/start/car-to-shop";
    }

    @PostMapping("/procesar-carrito")
    public String procesarCarrito(
            RedirectAttributes redirectAttributes,
            @RequestParam Integer idUsuario,
            @RequestParam String tarjeta
    ) {
        try {
            if (idUsuario == null || tarjeta == null || tarjeta.isEmpty()) {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "ID de Usuario o Tarjeta invalidos."
                );

                return "redirect:/start/car-to-shop";
            }

            Integer id = maintenanceCompra.registrarCompra(idUsuario, tarjeta);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "¿Te sientes exitado? Porque tu compra fue exitosa. El ID de la compra es: " + id
            );
            return "redirect:/start/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Erro en tu compra: " + e.getMessage()
            );

            return "redirect:/start/car-to-shop";
        }
    }

    @GetMapping("/compras-usuario")
    public String listarCompras(
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Integer id = maintenanceUsuario.idUsuario(principal);
            if (id == -1) {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/start/home";
            }

            List<DetalleCompraDto> comprasDetalladas = maintenanceCompra.listarComprasPorCliente(id);
            model.addAttribute("detallesCompra", comprasDetalladas);
            model.addAttribute("error", null);

            return "DetalleCompra";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ocurrio un error: " + e.getMessage());
            return "redirect:/start/home";
        }
    }

    @GetMapping("/add")
    public String add() {
        return "Nuevo-Producto";
    }

    @PostMapping("/add-new")
    public String addNew(
            @ModelAttribute CreateProductoDto createProducto,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            maintenanceProducto.createProducto(createProducto);
            redirectAttributes.addFlashAttribute("message", "Nuevo producto agregado");
            return "redirect:/start/products-all";
        } catch (Exception e) {
            model.addAttribute("error", "Error al agregar nuevo producto: " + e.getMessage());
            return "Nuevo-Producto";
        }
    }

    @GetMapping("/update/{id}")
    public String update(
            @PathVariable("id") Integer idProduct,
            @RequestParam Boolean estado,
            Model model
    ) {
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
    public String actualizarProducto(
            @ModelAttribute UpdateDetailProductoDto updateProducto,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Boolean updated = maintenanceProducto.updateProducto(updateProducto);

            if (Boolean.TRUE.equals(updated)) {
                redirectAttributes.addFlashAttribute("message", "Producto actualizado");
                return "redirect:/start/products-all";
            }

            model.addAttribute("error", "No se pudo actualizar el producto.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el producto: " + e.getMessage());
        }

        return "ActualizarProducto";
    }

    @PutMapping("/update-stock")
    public String cambiarStock(
            @ModelAttribute StockProductoDto stockProductoDto,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Boolean updated = maintenanceProducto.updateStockProducto(stockProductoDto);

            if (Boolean.FALSE.equals(updated)) {
                redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el stock.");
            }

            redirectAttributes.addFlashAttribute("message", "Stock Actualizado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el stock: " + e.getMessage());
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
                redirectAttributes.addFlashAttribute("message", respuesta1);
            } else {
                redirectAttributes.addFlashAttribute("message", respuesta2);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un error: " + e.getMessage());
        }

        return "redirect:/start/products-all";
    }
}
