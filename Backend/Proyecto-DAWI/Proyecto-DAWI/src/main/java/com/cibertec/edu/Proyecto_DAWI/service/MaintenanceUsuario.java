package com.cibertec.edu.Proyecto_DAWI.service;

import com.cibertec.edu.Proyecto_DAWI.dto.UsuarioDto;

public interface MaintenanceUsuario {
    UsuarioDto usuarioValidado(String email, String password);
}
