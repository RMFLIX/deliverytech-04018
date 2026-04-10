package com.deliveryth.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import com.deliveryth.delivery_api.dto.requests.RestauranteDTO;
import com.deliveryth.delivery_api.dto.responses.RestauranteResponseDTO;

public interface RestauranteService { RestauranteService cadastrarRestaurante(RestauranteDTO dto);
    RestauranteResponseDTO salve(RestauranteDTO dto);

    RestauranteResponseDTO obterPorId(Long id);

    List<RestauranteResponseDTO>listarTodosAtivos();

    List<RestauranteResponseDTO>filtrarPorCategotia(String categoria);

    RestauranteResponseDTO atualizarDados(Long id, RestauranteDTO dto);

    BigDecimal calcularFrete(Long id, String cep);

}