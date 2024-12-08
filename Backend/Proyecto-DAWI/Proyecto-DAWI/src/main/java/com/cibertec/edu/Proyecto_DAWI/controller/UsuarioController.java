package com.cibertec.edu.Proyecto_DAWI.controller;

import com.cibertec.edu.Proyecto_DAWI.dto.UsuarioDto;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private MaintenanceUsuario maintenanceUsuario;

    @PostMapping("/validar")
    public ResponseEntity<UsuarioDto> valdiarUsuario(@RequestParam String email, @RequestParam String password) {
        UsuarioDto usuarioDto = maintenanceUsuario.usuarioValidado(email, password);

        if (usuarioDto.idUsuario() == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(usuarioDto);
        }

        return ResponseEntity.ok(usuarioDto);
    }
}
