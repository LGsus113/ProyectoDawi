package com.cibertec.edu.Proyecto_DAWI.repository;

import com.cibertec.edu.Proyecto_DAWI.entity.Compra;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface CompraRepository extends CrudRepository<Compra, Integer> {
    @Modifying
    @CacheEvict(value = "productos", allEntries = true)
    @Query(value = "CALL sp_registrar_compra(:p_id_usu, :p_tarjeta, :p_detalle)", nativeQuery = true)
    void sp_registrar_compra(
            @Param("p_id_usu") Integer idUsuario,
            @Param("p_tarjeta") String tarjeta,
            @Param("p_detalle") String detalleJson
    );

    @Query(value = "CALL sp_registrar_compra_inicial(:p_id_usu, :p_tarjeta)", nativeQuery = true)
    Integer sp_registrar_compra_inicial(
            @Param("p_id_usu") Integer idUsuario,
            @Param("p_tarjeta") String tarjeta
    );

    @Modifying
    @CacheEvict(value = "productos", allEntries = true)
    @Query(value = "CALL sp_registrar_detalle_compra(:p_id_compra, :p_id_prod, :p_cantidad, :p_precio_unitario)", nativeQuery = true)
    void sp_registrar_detalle_compra(
            @Param("p_id_compra") Integer idCompra,
            @Param("p_id_prod") Integer idProducto,
            @Param("p_cantidad") Integer cantidad,
            @Param("p_precio_unitario") BigDecimal precioUnitario
    );

    @Modifying
    @Query(value = "CALL sp_actualizar_total_compra(:p_id_compra)", nativeQuery = true)
    void sp_actualizar_total_compra(
            @Param("p_id_compra") Integer idCompra
    );

}
