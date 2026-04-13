package com.deliveryth.delivery_api.dto.responses;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoResponseDTO {
    private Long id;
    private String nome;

    private String descricao;

    private String categoria;

    private BigDecimal preco;

    private boolean disponivel;

    private Long restauranteId;
}
