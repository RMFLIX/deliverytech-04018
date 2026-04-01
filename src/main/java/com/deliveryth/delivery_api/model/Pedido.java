package com.deliveryth.delivery_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pedidos")
public class Pedido {
    
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(name = "vl_total")
    private BigDecimal valorTotal;

@Column(name = "vl_taxa_entrega")
    private BigDecimal taxaEntrega;

@Column(name = "dt_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

@Column(name = "st_status")
    private String status;

@ManyToOne
@JoinColumn(name = "cliente_id")
    private Cliente cliente;

@ManyToOne
@JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;
}
