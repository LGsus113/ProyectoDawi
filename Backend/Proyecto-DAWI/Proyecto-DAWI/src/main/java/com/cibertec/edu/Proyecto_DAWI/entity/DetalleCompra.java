package com.cibertec.edu.Proyecto_DAWI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Detalle_Compra")
public class DetalleCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetaCompra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_com", referencedColumnName = "idCompra", nullable = false)
    private Compra compra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_prod", referencedColumnName = "idProducto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
}
