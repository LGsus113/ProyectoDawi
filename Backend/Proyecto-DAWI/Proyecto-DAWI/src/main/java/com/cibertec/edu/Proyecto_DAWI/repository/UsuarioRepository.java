package com.cibertec.edu.Proyecto_DAWI.repository;

import com.cibertec.edu.Proyecto_DAWI.entity.Usuario;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    @Cacheable(value = "user")
    @Procedure(name = "sp_autenticar_usuario")
    Usuario sp_autenticar_usuario(
            @Param("p_email") String email,
            @Param("p_contrasena") String password
    );
}
