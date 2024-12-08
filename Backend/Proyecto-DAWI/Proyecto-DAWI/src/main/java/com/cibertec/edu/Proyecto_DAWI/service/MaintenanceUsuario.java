package com.cibertec.edu.Proyecto_DAWI.service;

import com.cibertec.edu.Proyecto_DAWI.dto.UsuarioDto;
import org.springframework.cache.annotation.Cacheable;

public interface MaintenanceUsuario {
    UsuarioDto usuarioValidado(String email, String password);
}
