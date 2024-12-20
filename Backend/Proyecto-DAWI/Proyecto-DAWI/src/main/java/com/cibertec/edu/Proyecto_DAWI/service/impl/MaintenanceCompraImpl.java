package com.cibertec.edu.Proyecto_DAWI.service.impl;

import com.cibertec.edu.Proyecto_DAWI.repository.CompraRepository;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceCompra;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MaintenanceCompraImpl implements MaintenanceCompra {
    @Autowired
    private CompraRepository compraRepository;

    @Override
    public void registrarCompra(Integer idUsuario, String tarjeta, String detalleJson) throws Exception {
        compraRepository.sp_registrar_compra(idUsuario, tarjeta, detalleJson);
    }
}
