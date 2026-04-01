package com.deliveryth.delivery_api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import  jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(name = "nm_produto")
    private String nome;

@Column(name = "ds_descricao")
    private String descricao;

@Column(name = "vl_preco")
    private BigDecimal preco;

@Column(name = "tp_categoria")
    private String categoria;

@Column(name = "st_disponivel")
    private boolean disponivel = true;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;
}
