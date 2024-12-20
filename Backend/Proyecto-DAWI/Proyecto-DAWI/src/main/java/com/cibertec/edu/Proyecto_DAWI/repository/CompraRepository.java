package com.cibertec.edu.Proyecto_DAWI.repository;

import com.cibertec.edu.Proyecto_DAWI.entity.Compra;
import com.cibertec.edu.Proyecto_DAWI.entity.DetalleCompra;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface CompraRepository extends CrudRepository<Compra, Integer> {
    @Cacheable(value = "listaCompra")
    @Query(value = "CALL sp_listar_datos_compras_cliente(:p_id_usuario)", nativeQuery = true)
    Iterable<Compra> sp_listar_datos_compras_cliente(
            @Param("p_id_usuario") Integer idUsuario
    );

    @Cacheable(value = "listaDatosCompra")
    @Query(value = "CALL sp_listar_detalle_compra_cliente(:p_id_com)", nativeQuery = true)
    List<Object[]> sp_listar_detalle_compra_cliente(
            @Param("p_id_com") Integer idComp
    );

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
    @CacheEvict(value = {"productos", "listaDatosCompra"}, allEntries = true)
    @Query(value = "CALL sp_registrar_detalle_compra(:p_id_compra, :p_id_prod, :p_cantidad, :p_precio_unitario)", nativeQuery = true)
    void sp_registrar_detalle_compra(
            @Param("p_id_compra") Integer idCompra,
            @Param("p_id_prod") Integer idProducto,
            @Param("p_cantidad") Integer cantidad,
            @Param("p_precio_unitario") BigDecimal precioUnitario
    );

    @Modifying
    @CacheEvict(value = "listaCompra", allEntries = true)
    @Query(value = "CALL sp_actualizar_total_compra(:p_id_compra)", nativeQuery = true)
    void sp_actualizar_total_compra(
            @Param("p_id_compra") Integer idCompra
    );

}
