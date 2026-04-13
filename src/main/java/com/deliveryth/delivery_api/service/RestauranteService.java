package com.deliveryth.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliveryth.delivery_api.dto.requests.RestauranteDTO;
import com.deliveryth.delivery_api.dto.responses.RestauranteResponseDTO;
import com.deliveryth.delivery_api.exception.BusinessException;
import com.deliveryth.delivery_api.exception.EntityNotFoundException;
import com.deliveryth.delivery_api.model.Restaurante;
import com.deliveryth.delivery_api.repository.RestauranteRepository;

@Service 
public class RestauranteService { 
    
    private final RestauranteRepository repository; 
    private final ModelMapper mapper; 
    
    public RestauranteService(RestauranteRepository repository, ModelMapper mapper) { 
        this.repository = repository; 
        this.mapper = mapper; 
    } 
        
    @Transactional 
    public RestauranteResponseDTO cadastrar(RestauranteDTO dto) { 
        if (repository.existsByNome(dto.getNome())) { 
            throw new BusinessException("Restaurante com esse nome já cadastrado."); 
    } 
    
    Restaurante r = mapper.map(dto, Restaurante.class); 
    r.setAtivo(true); 
    r.setAvaliacao(BigDecimal.ZERO); 
    
    Restaurante salvo = repository.save(r); 
    return mapper.map(salvo, RestauranteResponseDTO.class); 
    } 
    
    public Page<RestauranteResponseDTO> listarAtivos(Pageable pageable) { 
        return repository.findByAtivoTrue(pageable) 
        .map(r -> mapper.map(r, RestauranteResponseDTO.class));
    }

    public Page<RestauranteResponseDTO> buscarPorCategoria(String categoria, Pageable pageable) { 
        return repository.findByCategoriaAndAtivoTrue(categoria, pageable) 
        .map(r -> mapper.map(r, RestauranteResponseDTO.class)); 
    } 
        
    public RestauranteResponseDTO buscarPorId(Long id) { 
        Restaurante r = repository.findById(id) 
        .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado.")); 
        return mapper.map(r, RestauranteResponseDTO.class); 
    } 
        
    @Transactional
    public RestauranteResponseDTO toggle(Long id) { 
        Restaurante restaurante = repository.findById(id) 
        .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado.")); 
        
        restaurante.setAtivo(!restaurante.isAtivo()); 
        return mapper.map(restaurante, RestauranteResponseDTO.class); 
    } 
}