package com.deliveryth.delivery_api.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotalVendasPorRestauranteDTO {
    private String nomeRestaurante;
    private BigDecimal totalVendas;
}
