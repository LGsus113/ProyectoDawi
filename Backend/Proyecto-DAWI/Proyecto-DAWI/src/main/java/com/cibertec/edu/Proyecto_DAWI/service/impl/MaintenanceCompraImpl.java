package com.cibertec.edu.Proyecto_DAWI.service.impl;

import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.CompraDataDto;
import com.cibertec.edu.Proyecto_DAWI.repository.CompraRepository;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceCompra;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class MaintenanceCompraImpl implements MaintenanceCompra {
    @Autowired
    private CompraRepository compraRepository;

    @Override
    public void registrarCompra1(Integer idUsuario, String tarjeta, String detalleJson) throws Exception {
        compraRepository.sp_registrar_compra(idUsuario, tarjeta, detalleJson);
    }

    @Override
    public Integer registrarCompra(Integer idUsuario, String tarjeta, List<CompraDataDto> detalle) throws Exception {
        Integer idCompra = compraRepository.sp_registrar_compra_inicial(idUsuario, tarjeta);

        for (CompraDataDto dto : detalle) {
            compraRepository.sp_registrar_detalle_compra(
                    idCompra,
                    dto.id_prod(),
                    dto.cantidad(),
                    dto.precio_unitario()
            );
        }

        compraRepository.sp_actualizar_total_compra(idCompra);

        return idCompra;
    }
}
