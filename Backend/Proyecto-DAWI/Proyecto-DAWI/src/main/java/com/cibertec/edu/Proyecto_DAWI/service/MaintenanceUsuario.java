package com.cibertec.edu.Proyecto_DAWI.service;

import com.cibertec.edu.Proyecto_DAWI.dto.UsuarioDto;
import com.cibertec.edu.Proyecto_DAWI.entity.Usuario;

import java.util.Optional;

public interface MaintenanceUsuario {
    UsuarioDto usuarioValidado(String email, String password);

    UsuarioDto usuario(String username);
}
