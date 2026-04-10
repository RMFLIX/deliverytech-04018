package com.deliveryth.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deliveryth.delivery_api.dto.requests.RestauranteDTO;
import com.deliveryth.delivery_api.dto.responses.RestauranteResponseDTO;
import com.deliveryth.delivery_api.repository.RestauranteRepository;

@Service
public class RestauranteServiceImpl implements RestauranteService {
    
    @Autowired
    private RestauranteRepository repository;

    @Override
    public RestauranteResponseDTO salve(RestauranteDTO dto){
        return null;
    }

    @Override
    public List<RestauranteResponseDTO>filtrarPorCategoria(String categoria){
        return null;
    }

    @Override
    public RestauranteResponseDTO atualizarDados(Long id, RestauranteDTO dto){
        return null;
    }

    @Override
    public BigDecimal calcularFrete(Long id, String cep){
        return null;
    }

    @Override
    public RestauranteService cadastrarRestaurante(RestauranteDTO dto) {
       return null;
    }

    @Override
    public RestauranteResponseDTO obterPorId(Long id) {
        return null;
    }

    @Override
    public List<RestauranteResponseDTO> listarTodosAtivos() {
        return null;
    }

}
