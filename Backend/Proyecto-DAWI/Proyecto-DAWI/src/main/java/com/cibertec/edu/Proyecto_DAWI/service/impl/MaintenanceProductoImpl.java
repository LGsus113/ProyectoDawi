package com.cibertec.edu.Proyecto_DAWI.service.impl;

import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto;
import com.cibertec.edu.Proyecto_DAWI.entity.Producto;
import com.cibertec.edu.Proyecto_DAWI.repository.ProductoRepository;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
}
