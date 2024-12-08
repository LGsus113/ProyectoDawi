package com.cibertec.edu.Proyecto_DAWI.dto;

import com.cibertec.edu.Proyecto_DAWI.entity.Usuario;

public record UsuarioDto(
        Integer idUsuario,
        String nombre,
        Usuario.Rol rol,
        Boolean estado
) {
}
