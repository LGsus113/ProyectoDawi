package com.cibertec.edu.Proyecto_DAWI.repository;

import com.cibertec.edu.Proyecto_DAWI.entity.Producto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ProductoRepository extends CrudRepository<Producto, Integer> {
    @Cacheable(value = "productos")
    @Procedure(name = "sp_listar_productos")
    Iterable<Producto> sp_listar_productos(
            @Param("p_disponibles") boolean disponibles
    );

    @CacheEvict(value = "productos", allEntries = true)
    @Query(value = "CALL sp_habilitar_producto(:p_id_prod)", nativeQuery = true)
    List<Map<String, Object>> sp_habilitar_producto(
            @Param("p_id_prod") Integer idProducto
    );

    @CacheEvict(value = "productos", allEntries = true)
    @Query(value = "CALL sp_deshabilitar_producto(:p_id_prod)", nativeQuery = true)
    List<Map<String, Object>> sp_deshabilitar_producto(
            @Param("p_id_prod") Integer idProducto
    );

    @CacheEvict(value = "productos", allEntries = true)
    Producto save(Producto p);
}
