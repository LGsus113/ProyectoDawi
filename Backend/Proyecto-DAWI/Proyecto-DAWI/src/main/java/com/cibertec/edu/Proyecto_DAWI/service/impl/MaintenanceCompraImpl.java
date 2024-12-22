package com.cibertec.edu.Proyecto_DAWI.service.impl;

import com.cibertec.edu.Proyecto_DAWI.dto.CompraDto.*;
import com.cibertec.edu.Proyecto_DAWI.dto.ProductoDto.ProductoDto;
import com.cibertec.edu.Proyecto_DAWI.entity.Compra;
import com.cibertec.edu.Proyecto_DAWI.entity.DetalleCompra;
import com.cibertec.edu.Proyecto_DAWI.repository.CompraRepository;
import com.cibertec.edu.Proyecto_DAWI.service.MaintenanceCompra;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class MaintenanceCompraImpl implements MaintenanceCompra {
    @Autowired
    private CompraRepository compraRepository;

    private final List<ProductCompleteDto> listComprar = new ArrayList<>();

    private List<DetalleCompra> setManual(List<Object[]> resultados) {
        List<DetalleCompra> listaDetalleCompra = new ArrayList<>();

        for (Object[] fila : resultados) {
            DetalleCompra detalle = new DetalleCompra();
            detalle.setIdDetaCompra((Integer) fila[0]);
            detalle.setIdCompra((Integer) fila[1]);
            detalle.setIdProducto((Integer) fila[2]);
            detalle.setNombre((String) fila[3]);
            detalle.setCantidad((Integer) fila[4]);
            detalle.setPrecioUnitario((BigDecimal) fila[5]);
            detalle.setSubtotal((BigDecimal) fila[6]);

            listaDetalleCompra.add(detalle);
        }

        return listaDetalleCompra;
    }

    @Override
    public List<DetalleCompraDto> listarComprasPorCliente(Integer idUsuario) throws Exception {
        Iterable<Compra> compras = compraRepository.sp_listar_datos_compras_cliente(idUsuario);
        Iterable<DetalleCompra> listaDetalleCompra = setManual(compraRepository.sp_listar_detalle_compra_cliente(idUsuario));

        List<DatosNecesariosDto> comprasDto = StreamSupport.stream(compras.spliterator(), false)
                .map(compra -> new DatosNecesariosDto(
                        compra.getIdCompra(),
                        compra.getFecha(),
                        compra.getTotal()
                )).toList();

        Map<Integer, List<DetalleCompra>> detallesPorCompra = StreamSupport.stream(listaDetalleCompra.spliterator(), false)
                .collect(Collectors.groupingBy(DetalleCompra::getIdCompra));

        return comprasDto.stream()
                .map(compraDto -> {
                    List<DetalleCompra> detalles = detallesPorCompra.get(compraDto.id_compra());

                    List<ProductoCompraDto> productosDto = detalles.stream()
                            .map(detalle -> new ProductoCompraDto(
                                    detalle.getIdCompra(),
                                    detalle.getNombre(),
                                    detalle.getPrecioUnitario(),
                                    detalle.getCantidad(),
                                    detalle.getSubtotal()
                            )).toList();

                    return new DetalleCompraDto(
                            compraDto.id_compra(),
                            compraDto.fecha(),
                            compraDto.total(),
                            productosDto
                    );
                }).toList();
    }

    @Override
    public void registrarCompra1(Integer idUsuario, String tarjeta, String detalleJson) throws Exception {
        compraRepository.sp_registrar_compra(idUsuario, tarjeta, detalleJson);
    }

    @Override
    public Integer registrarCompra(Integer idUsuario, String tarjeta) throws Exception {
        Integer idCompra = compraRepository.sp_registrar_compra_inicial(idUsuario, tarjeta);

        List<CompraDataDto> detalleDeVenta = listComprar.stream()
                .map(prod -> new CompraDataDto(
                        prod.idProducto(),
                        prod.cantidad(),
                        prod.precio()
                )).toList();

        for (CompraDataDto dto : detalleDeVenta) {
            compraRepository.sp_registrar_detalle_compra(
                    idCompra,
                    dto.id_prod(),
                    dto.cantidad(),
                    dto.precio_unitario()
            );
        }

        compraRepository.sp_actualizar_total_compra(idCompra);

        listComprar.clear();

        return idCompra;
    }

    @Override
    public List<ProductCompleteDto> listarCarrito() throws Exception {
        return listComprar;
    }

    @Override
    public BigDecimal totalCarrito() throws Exception {
        BigDecimal total = BigDecimal.ZERO;

        for (ProductCompleteDto p : listComprar) {
            total = total.add(p.totalCoste());
        }

        return total;
    }

    @Override
    public Boolean agregarCarrito(List<ProductoDto> p, Integer id) throws Exception {
        ProductoDto producto = p.stream().filter(pr -> pr.idProducto().equals(id)).findFirst().orElse(null);

        if (producto != null) {
            ProductCompleteDto productCompleteDto = new ProductCompleteDto(
                    producto.idProducto(),
                    producto.nombre(),
                    producto.precio(),
                    1,
                    producto.stock(),
                    producto.precio()
            );
            listComprar.add(productCompleteDto);

            return true;
        }

        return false;
    }

    @Override
    public Boolean removerCarrito(Integer id) throws Exception {
        return listComprar.removeIf(product -> product.idProducto().equals(id));
    }

    @Override
    public Boolean actualizarCarrito(Integer id, Integer cantidad) throws Exception {
        for (ProductCompleteDto p : listComprar) {
            if (p.idProducto().equals(id)) {
                listComprar.set(
                        listComprar.indexOf(p),
                        new ProductCompleteDto(
                                p.idProducto(),
                                p.nombre(),
                                p.precio(),
                                cantidad,
                                p.stock(),
                                p.precio().multiply(new BigDecimal(cantidad))
                        )
                );

                return true;
            }
        }

        return false;
    }


}
