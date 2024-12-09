package com.cibertec.edu.Proyecto_DAWI.repository;

import com.cibertec.edu.Proyecto_DAWI.entity.Producto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends CrudRepository<Producto, Integer> {
    @Cacheable(value = "productos")
    @Procedure(name = "sp_listar_productos")
    Iterable<Producto> sp_listar_productos(
            @Param("p_disponibles") boolean disponibles
    );
}
