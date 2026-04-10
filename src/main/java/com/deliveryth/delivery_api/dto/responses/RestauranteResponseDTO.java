package com.deliveryth.delivery_api.dto.responses;


public record RestauranteResponseDTO( 
    Long id,
    String nome,
    String categoria,
    Double taxaEntrega,
    Boolean ativo
){}
