package com.deliveryth.delivery_api.dto.responses;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Representação detalhada de um produto para resposta da API")
public class ProdutoResponseDTO {
    
    @Schema(description = "Identificador único do produto", example = "1")
    private Long id;
    
    @Schema(description = "Nome produto", example =  "X-Burguer Especial")
    private String nome;

    @Schema(description = "Descrição detalhada dos ingredientes", example = "Pão de brioche, carne 180g, queijo cheddar e maionese artesanal")
    private String descricao;

    @Schema(description = "Categoria do item no menu", example = "LANCHES")
    private String categoria;

    @Schema(description = "Preço unitário", example = "32.50")
    private BigDecimal preco;

    @Schema(description = "Indica se o produto está disponível para venda no momento", example = "true")
    private boolean disponivel;

    @Schema(description = "ID do restaurante proprietário deste produto", example = "10")
    private Long restauranteId;
}
