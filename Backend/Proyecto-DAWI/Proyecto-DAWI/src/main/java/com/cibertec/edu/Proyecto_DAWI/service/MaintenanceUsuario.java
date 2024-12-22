package com.cibertec.edu.Proyecto_DAWI.service;

import com.cibertec.edu.Proyecto_DAWI.dto.UsuarioDto;

import java.security.Principal;

public interface MaintenanceUsuario {
    UsuarioDto usuarioValidado(String email, String password);

    Integer idUsuario(Principal principal) throws Exception;
}
