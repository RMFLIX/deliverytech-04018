package com.deliveryth.delivery_api.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "produtos")
@Schema (description = "Entidade que representa um item do menu de um restaurante.")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema (description = "Identificador único do produto", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "O nome produto é obrigatório")
    @Column(name = "produto")
    @Schema (description = "Nome do prato ou item", example = "Hambúrguer Artesanal Bacon")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(name = "descricao")
    @Schema(description = "Detalhes sobre os ingredientes ou composição", example = "Pão brioche, carne 180g, bacon crocante e molho da casa")
    private String descricao;

    @NotNull (message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    @Column(name = "preco")
    @Schema(description = "Preço unitário do produto",  example = "15.90")
    private BigDecimal preco;

    @NotBlank (message = "A categoria é obrigatória")
    @Column(name = "categoria")
    @Schema(description = "Tipo de produto", example = "LANCHES")
    private String categoria;

    @Column(name = "disponivel")
    @Schema(description = "Define se o produto aparece no menu para os clientes", example = "true")
    private boolean disponivel = true;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    @Schema(description = "Restaurante ao qual este produto pertence")
    private Restaurante restaurante;
}
