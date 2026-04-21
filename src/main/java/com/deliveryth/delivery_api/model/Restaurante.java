package com.deliveryth.delivery_api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.deliveryth.delivery_api.enums.CategoriaRestaurante;

import io.swagger.v3.oas.annotations.media.Schema;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurante")
@Schema(description = "Entidade que representa um restaurante na plataforma de delivery.")
public class Restaurante {

    @OneToOne
    @JoinColumn(name = "usuario_id")
    @Schema(description = "Utilizador responsável/dono de restaurante.")
    private Usuario usuario;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do restaurante", example="1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "O nome não pode estar vazio")
    @Column(name = "restaurante")
    @Schema (description = "Nome fantasia do restaurante", example = "Pizzaria Bela Nápoles")
    private String nome;

    @NotNull( message = "A categoria é obrigatória")
    @Column(name = "categoria")
    @Schema(description = "Categoria gastronómica do restaurante", example = "ITALIANA")
    private CategoriaRestaurante categoria;

    @NotBlank(message = "O endereço é obrigatório")
    @Column(name = "endereco")
    @Schema(description = "Endereço completo para entregas/recolhas", example = "Rua das Flores, 123 - Centro")
    private String endereco;

    @NotBlank(message = "O telefone é obrigatório")
    @Column(name = "telefone")
    @Schema(description = "Número de telefone de contato", example = "(13) 91111-0101")
    private String telefone;

     /* @Column(name = taxa_entrega) private BigDecimal taxaEntrega;  */

    @PositiveOrZero(message = "A avaliação não pode ser negativa")
    @Column(name = "avaliacao")
    @Schema(description = "Nota média da avaliação do restaurante (0.0 a 5.0", example = "4.5")
    private BigDecimal avaliacao;

    @Column(name = "ativo")
    @Schema(description = "Indica se o restaurante está aberto para receber pedidos", example = "true")
    private boolean ativo = true;
    
    @OneToMany(mappedBy = "restaurante", fetch=FetchType.LAZY)
    @Schema(description = "Lista de produtos disponíveis no menu do restaurante")
    private List<Produto> produtos = new ArrayList<>();

    @OneToMany(mappedBy = "restaurante", fetch=FetchType.LAZY)
    @Schema(description = "Histórico de pedidos realizados neste restaurante")
    private List<Pedido> pedidos = new ArrayList<>();

}
