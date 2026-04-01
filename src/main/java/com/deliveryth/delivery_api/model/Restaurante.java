package com.deliveryth.delivery_api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurante")
public class Restaurante {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

@Column(name = "nm_restaurante")
     private String nome;

@Column(name = "tp_categoria")
     private String categoria;

@Column(name = "ds_endereco")
     private String endereco;

@Column(name = "nr_telefone")
     private String telefone;

     /* @Column(name = taxa_entrega) private BigDecimal taxaEntrega;  */

@Column(name = "vl_avaliacao")
    private BigDecimal avaliacao;

@Column(name = "st_ativo")
    private boolean ativo = true;
    
    @OneToMany(mappedBy = "restaurante", fetch=FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();
}
