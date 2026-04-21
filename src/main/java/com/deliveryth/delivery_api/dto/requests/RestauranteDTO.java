package com.deliveryth.delivery_api.dto.requests;

import java.math.BigDecimal;

import com.deliveryth.delivery_api.validation.CategoriaValida;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Objeto de transferência para cadastro ou atualização de um restaurante")
public class RestauranteDTO {

   @NotBlank(message = "Nome do restaurante é obrigatório")
   @Schema(description = "Nome fantasia do restaurante", example = "Sabor & Arte Delivery")
    private String nome;

   @NotBlank(message = "Categiria é obrigatória")
   @CategoriaValida
   @Schema(description = "Categoria do restaurante (deve ser uma categoria existe", example = "BRASILEIRA")
   private String categoria;

   @Size(min = 5, max = 255, message = "Endereço deve ter entre 5 e 255 caracteres")
   @Schema(description = "Endereço completo da unidade", example = "Rua das Flores, 123, Centro")
   private String endereco;

   @NotBlank(message = "Telefone é obrigatório")
   @Pattern(
      regexp = "\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}",
      message = "Telefone inválido. Formato esperado: (xx) xxxxx-xxxx ou similar")
   @Schema(description = "Telefone de contato formatado", example = "(13)91111-0101")
    private String telefone;

    @NotNull(message = "A taxa de entrega é obrigatória")
    @Schema(description = "Valor cobrado pelo frete", example = "5.50")
    private BigDecimal taxaEntrega;
       
    }

