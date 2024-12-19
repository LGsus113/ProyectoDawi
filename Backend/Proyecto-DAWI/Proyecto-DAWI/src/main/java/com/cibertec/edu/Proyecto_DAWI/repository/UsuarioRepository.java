package com.cibertec.edu.Proyecto_DAWI.repository;

import com.cibertec.edu.Proyecto_DAWI.entity.Usuario;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    @Cacheable(value = "userRest")
    @Procedure(name = "sp_autenticar_usuario")
    Optional<Usuario> sp_autenticar_usuario(
            @Param("p_email") String email,
            @Param("p_contrasena") String password
    );

    Optional<Usuario> findByEmail(String email);

    @Cacheable(value = "user")
    Optional<Usuario> findByNombre(String username);
}
