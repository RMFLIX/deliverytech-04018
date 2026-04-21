package com.deliveryth.delivery_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.deliveryth.delivery_api.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pedidos")
@Schema(description = "Entidade que representa um pedido realizado por um cliente.")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do pedido", example = "500", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "data_pedido")
    @NotNull
    @Schema(description = "Data e hora em que o pedido foi registrado", example = "2026-04-21T10:00:00")
    private LocalDateTime dataPedido;

    @Column(name = "endereco_entrega")
    @NotBlank(message = "O endereço de entrega é obrigatório")
    @Schema(description = "Endereço completo onde o pedido deve ser entregue", example = "Avenida Central, 456, Apt 22")
    private String enderecoEntrega;

    @Column(name = "taxa_entrega")
    @NotNull
    @Positive
    @Schema(description = "Custo do frete para este pedido", example = "7.50")
    private BigDecimal taxaEntrega;

    @Column(name = "valor_total")
    @NotNull
    @Positive
    @Schema(description = "Valor total somado dos itens mais a taxa de entrega", example = "65.90")
    private BigDecimal valorTotal;
    
    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description  = "Estado atual do processamento do pedido", example = "PENDENTE")
    private StatusPedido status;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @Schema(description = "Informações do cliente que realizou o pedido")
    private Cliente cliente;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    @Schema(description = "Restaurante que recebeu o pedido")
    private Restaurante restaurante;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido", cascade =  CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista detalhada de produtos e quantidades deste pedido")
    private List<ItemPedido> itens = new ArrayList<>();


    @PrePersist
    public void PrePersist(){
        this.dataPedido = LocalDateTime.now();
    }
}
