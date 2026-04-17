package com.deliveryth.delivery_api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.deliveryth.delivery_api.enums.CategoriaRestaurante;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurante")
public class Restaurante {

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurante")
    private String nome;

    @Column(name = "categoria")
    private CategoriaRestaurante categoria;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "telefone")
    private String telefone;

     /* @Column(name = taxa_entrega) private BigDecimal taxaEntrega;  */

    @Column(name = "avaliacao")
    private BigDecimal avaliacao;

    @Column(name = "ativo")
    private boolean ativo = true;
    
    @OneToMany(mappedBy = "restaurante", fetch=FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();

    @OneToMany(mappedBy = "restaurante", fetch=FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();

}
