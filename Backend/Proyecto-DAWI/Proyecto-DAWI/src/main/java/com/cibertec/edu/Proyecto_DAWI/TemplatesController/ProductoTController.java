package com.cibertec.edu.Proyecto_DAWI.TemplatesController;

import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.CompraDataDto;
import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.DetalleCompraDto;
import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.ProductCompleteDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.CreateProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.ProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.StockProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.UpdateDetailProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.UsuarioDto;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceCompra;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceProducto;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceUsuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/start")
public class ProductoTController {
    @Autowired
    MaintenanceProducto maintenanceProducto;

    @Autowired
    MaintenanceUsuario maintenanceUsuario;

    @Autowired
    MaintenanceCompra maintenanceCompra;

    private List<ProductCompleteDto> listComprar = new ArrayList<>();

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
    public String enviar(@PathVariable("id") Integer idProduct) {
        try {
            List<ProductoDto> productos = maintenanceProducto.productosPorDisponibilidad(true);
            ProductoDto productoDto = productos.stream().filter(p -> p.idProducto().equals(idProduct)).findFirst().orElse(null);

            if (productoDto != null) {
                ProductCompleteDto producto = new ProductCompleteDto(
                        productoDto.idProducto(),
                        productoDto.nombre(),
                        productoDto.precio(),
                        1,
                        productoDto.stock(),
                        productoDto.precio()
                );
                listComprar.add(producto);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return "redirect:/start/home";
    }

    @GetMapping("/del-cart/{id}")
    public String quitar(@PathVariable("id") Integer idProduct) {
        listComprar.removeIf(product -> product.idProducto().equals(idProduct));
        return "redirect:/start/car-to-shop";
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

            BigDecimal totalCarrito = BigDecimal.ZERO;
            for (ProductCompleteDto p : listComprar) {
                totalCarrito = totalCarrito.add(p.totalCoste());
            }

            model.addAttribute("idUsuario", id);
            model.addAttribute("productoTable", listComprar);
            model.addAttribute("totalCarrito", totalCarrito);

            return "Car";
        } catch (Exception e) {
            System.out.println("Error en el metodo: " + e.getMessage());
            return "redirect:/start/home";
        }
    }

    @GetMapping("/update-cart/{id}")
    public String updateCart(
            @PathVariable Integer id,
            @RequestParam Integer cantidad
    ) {
        for (ProductCompleteDto p : listComprar) {
            if (p.idProducto().equals(id)) {
                listComprar.set(
                        listComprar.indexOf(p),
                        new ProductCompleteDto(
                                p.idProducto(),
                                p.nombre(),
                                p.precio(),
                                cantidad,
                                p.stock(),
                                p.precio().multiply(new BigDecimal(cantidad))
                        )
                );
                break;
            }
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
                return "redirect:/start/car-to-shop?error=InvalidInput";
            }

            List<CompraDataDto> productsList = listComprar.stream()
                    .map(prod -> new CompraDataDto(
                            prod.idProducto(),
                            prod.cantidad(),
                            prod.precio()
                    )).collect(Collectors.toList());

            Integer id = maintenanceCompra.registrarCompra(idUsuario, tarjeta , productsList);

            listComprar.clear();

            redirectAttributes.addFlashAttribute(
                    "message",
                    "¿Te sientes exitado? Porque tu compra fue exitosa. El ID de la compra es: " + id
            );
            return "redirect:/start/home";
        } catch (Exception e) {
            System.out.println("Error en: " + e.getMessage());
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("message", "Erro en tu compra.");

            return "redirect:/start/car-to-shop";
        }
    }

    @GetMapping("/compras-usuario")
    public String listarCompras(Model model, Principal principal) {
        try {
            UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
            String username = userDetails.getUsername();

            UsuarioDto usuario = maintenanceUsuario.usuario(username);

            if (usuario == null) {
                System.out.println("Usuario no encontrado para el email: " + username);
                return "redirect:/start/home";
            }

            Integer id = usuario.idUsuario();

            List<DetalleCompraDto> comprasDetalladas = maintenanceCompra.listarComprasPorCliente(id);
            model.addAttribute("detallesCompra", comprasDetalladas);
            model.addAttribute("error", null);
            return "DetalleCompra";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ocurrio un error: " + e.getMessage());
            return "redirect:/start/home";
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
                redirectAttributes.addFlashAttribute("success", respuesta2);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hubo un error: " + e.getMessage());
        }

        return "redirect:/start/products-all";
    }
}
