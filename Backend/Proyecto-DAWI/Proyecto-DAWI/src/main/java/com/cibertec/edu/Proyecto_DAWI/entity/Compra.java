package com.cibertec.edu.Proyecto_DAWI.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Compras")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCompra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usu", referencedColumnName = "idUsuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(nullable = false, length = 16)
    private String tarjeta;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
}
