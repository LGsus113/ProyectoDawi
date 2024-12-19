package com.cibertec.edu.Proyecto_DAWI.repository;

import com.cibertec.edu.Proyecto_DAWI.entity.Compra;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CompraRepository extends CrudRepository<Compra, Integer> {
    @Modifying
    @CacheEvict(value = "productos", allEntries = true)
    @Query(value = "CALL sp_registrar_compra(:p_id_usu, :p_tarjeta, :p_detalle)", nativeQuery = true)
    void sp_registrar_compra(
            @Param("p_id_usu") Integer idUsuario,
            @Param("p_tarjeta") String tarjeta,
            @Param("p_detalle") String detalleJson
    );
}
