package com.cibertec.edu.Proyecto_DAWI.controller;

import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.CompraDataDto;
import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.CompraRequestDto;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceCompra;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compras")
public class ComproController {
    @Autowired
    MaintenanceCompra maintenanceCompra;

    @PostMapping("/registrar")
    public String registrarCompra(@RequestBody CompraRequestDto compraRequest) {
        try {
            Integer idUsuario = compraRequest.idUsuario();
            String tarjeta = compraRequest.tarjeta();
            List<CompraDataDto> detalleJson = compraRequest.detalleJson();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(detalleJson);

            maintenanceCompra.registrarCompra1(idUsuario, tarjeta, jsonString);

            return "Registro exitoso";
        } catch (DataAccessException e) {
            return "Error de acceso: " + e.getMessage();
        } catch (Exception e) {
            return "Hubo un error al registrar, es el siguiente: " + e.getMessage();
        }
    }
}
