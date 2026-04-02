package com.deliveryth.delivery_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.deliveryth.delivery_api.enums.StatusPedido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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

    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;

    @Column(name = "endereco_entrega")
    private String enderecoEntrega;

/* @Column(name = "numero_pedido")
    private  String numeroPedido; */

    @Column(name = "taxa_entrega")
    private BigDecimal taxaEntrega;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status_Pedido")
    private StatusPedido status;

    @Column(name = "Valor_total")
    private BigDecimal valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

   @PrePersist
   public void PrePersist(){
        this.dataPedido = LocalDateTime.now();
    }
}
