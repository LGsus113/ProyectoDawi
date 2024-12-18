package com.cibertec.edu.Proyecto_DAWI.service.impl;

import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.CreateProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.ProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.StockProductoDto;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.UpdateDetailProductoDto;
import com.cibertec.edu.Proyecto_DAWI.entity.Producto;
import com.cibertec.edu.Proyecto_DAWI.repository.ProductoRepository;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MaintenanceProductoImpl implements MaintenanceProducto {
    @Autowired
    ProductoRepository productoRepository;

    @Override
    public List<ProductoDto> productosPorDisponibilidad(boolean disponibilidad) {
        List<ProductoDto> productos = new ArrayList<ProductoDto>();
        Iterable<Producto> iterable = productoRepository.sp_listar_productos(disponibilidad);

        iterable.forEach(producto -> {
            ProductoDto productoDto = new ProductoDto(
                    producto.getIdProducto(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getStock(),
                    producto.getEstado()
            );

            productos.add(productoDto);
        });

        return productos;
    }

    @Override
    public Page<ProductoDto> productosPorDisponibilidadPaginado(boolean disponibilidad, int page, int size) {
        List<ProductoDto> productos = new ArrayList<ProductoDto>();
        Iterable<Producto> iterable = productoRepository.sp_listar_productos(disponibilidad);

        iterable.forEach(producto -> {
            ProductoDto productoDto = new ProductoDto(
                    producto.getIdProducto(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getStock(),
                    producto.getEstado()
            );

            productos.add(productoDto);
        });

        int start = Math.min(page * size, productos.size());
        int end = Math.min((page + 1) * size, productos.size());
        List<ProductoDto> pagedProductos = productos.subList(start, end);

        return new PageImpl<>(pagedProductos, PageRequest.of(page, size), productos.size());
    }

    @Override
    public void createProducto(CreateProductoDto createProducto) {
        Producto p = new Producto(
                createProducto.nombre(),
                createProducto.descripcion(),
                createProducto.precio(),
                createProducto.stock(),
                true
        );

        productoRepository.save(p);
    }

    @Override
    public Boolean updateProducto(UpdateDetailProductoDto updateProducto) {
        Optional<Producto> optional = productoRepository.findById(updateProducto.idProducto());

        return optional.map(
                producto -> {
                    producto.setNombre(updateProducto.nombre());
                    producto.setDescripcion(updateProducto.descripcion());
                    producto.setPrecio(updateProducto.precio());

                    productoRepository.save(producto);
                    return true;
                }
        ).orElse(false);
    }

    @Override
    public Boolean updateStockProducto(StockProductoDto stockProductoDto) {
        Optional<Producto> optional = productoRepository.findById(stockProductoDto.idProducto());

        return optional.map(
                producto -> {
                    producto.setStock(stockProductoDto.stock());

                    productoRepository.save(producto);
                    return true;
                }
        ).orElse(false);
    }

    @Override
    public Boolean habilitarProductos(Integer idProducto) {
        List<Map<String, Object>> resultado = productoRepository.sp_habilitar_producto(idProducto);

        if (resultado != null && !resultado.isEmpty()) {
            Object resultadoValor = resultado.get(0).get("resultado");

            if (resultadoValor instanceof Integer) {
                return (Integer) resultadoValor == 1;
            }
        }

        return false;
    }

    @Override
    public Boolean deshabilitarProductos(Integer idProducto) {
        List<Map<String, Object>> resultado = productoRepository.sp_deshabilitar_producto(idProducto);

        if (resultado != null && !resultado.isEmpty()) {
            Object resultadoValor = resultado.get(0).get("resultado");

            if (resultadoValor instanceof Integer) {
                return (Integer) resultadoValor == 1;
            }
        }

        return false;
    }
}
