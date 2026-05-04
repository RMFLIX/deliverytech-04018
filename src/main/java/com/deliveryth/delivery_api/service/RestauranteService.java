package com.deliveryth.delivery_api.service;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliveryth.delivery_api.dto.requests.PedidoDTO;
import com.deliveryth.delivery_api.dto.requests.RestauranteDTO;
import com.deliveryth.delivery_api.dto.responses.RestauranteResponseDTO;
import com.deliveryth.delivery_api.enums.CategoriaRestaurante;
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

    @CacheEvict(value = "restaurantes", allEntries = true)
    @Transactional 
    public RestauranteResponseDTO cadastrar(RestauranteDTO dto) { 
        if (repository.existsByNome(dto.getNome())) { 
            throw new BusinessException("Restaurante com esse nome já cadastrado."); 
    } 
    
    CategoriaRestaurante categoriaEnum = CategoriaRestaurante.valueOf(dto.getCategoria().toLowerCase());
    
    Restaurante r = mapper.map(dto, Restaurante.class); 
    r.setCategoria(categoriaEnum);
    r.setAtivo(true); 
    r.setAvaliacao(BigDecimal.ZERO); 
    
    Restaurante salvo = repository.save(r); 
    return mapper.map(salvo, RestauranteResponseDTO.class); 
    } 
    
    @Cacheable(value = "restaurantes", key = "'todos'")
    public Page<RestauranteResponseDTO> listarAtivos(Pageable pageable) { 
       System.out.print("BUSCOU NO BANCO!");
        try{
        Thread.sleep(2000);
       }catch(InterruptedException e){
        Thread.currentThread().interrupt();
       }

        return repository.findByAtivoTrue(pageable) 
                .map(r -> mapper.map(r, RestauranteResponseDTO.class));
    }

    public Page<RestauranteResponseDTO> buscarPorCategoria(String categoria, Pageable pageable) { 
        CategoriaRestaurante categoriaEnum;

        try{
            categoriaEnum = CategoriaRestaurante.valueOf(categoria.toUpperCase());
        }catch(IllegalArgumentException e){
            throw new BusinessException("Categoria inválida.");
        }

        return repository.findByCategoriaAndAtivoTrue(categoriaEnum, pageable)
        .map(r -> mapper.map(r, RestauranteResponseDTO.class)); 
    } 
       
    @Cacheable(value = "restaurantes", key = "#id")
    public RestauranteResponseDTO buscarPorId(Long id) { 
    try{
        Thread.sleep(2000);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

        Restaurante r = repository.findById(id) 
        .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado.")); 
        return mapper.map(r, RestauranteResponseDTO.class); 
    } 

    @CacheEvict(value = "restaurantes", key = "#id")
    @Transactional
    public RestauranteResponseDTO toggle(Long id) { 
        Restaurante restaurante = repository.findById(id) 
        .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado.")); 
        
        restaurante.setAtivo(!restaurante.isAtivo()); 
        return mapper.map(restaurante, RestauranteResponseDTO.class); 
    } 

}