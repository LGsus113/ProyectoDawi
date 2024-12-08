package com.cibertec.edu.Proyecto_DAWI.service.impl;

import com.cibertec.edu.Proyecto_DAWI.dto.UsuarioDto;
import com.cibertec.edu.Proyecto_DAWI.entity.Usuario;
import com.cibertec.edu.Proyecto_DAWI.repository.UsuarioRepository;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MaintenanceUsuarioImpl implements MaintenanceUsuario {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UsuarioDto usuarioValidado(String email, String password) {
        Usuario usuario = usuarioRepository.sp_autenticar_usuario(email, password);

        return Optional.ofNullable(usuario).map(
                u -> new UsuarioDto(
                        u.getIdUsuario(),
                        u.getNombre(),
                        u.getRol(),
                        u.getEstado()
                )
        ).orElse(
                new UsuarioDto(
                        -1,
                        "Usuario no encontrado",
                        Usuario.Rol.Usuario,
                        false
                )
        );
    }
}