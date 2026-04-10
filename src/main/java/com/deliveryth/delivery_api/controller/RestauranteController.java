package com.deliveryth.delivery_api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliveryth.delivery_api.dto.requests.RestauranteDTO;
import com.deliveryth.delivery_api.dto.responses.RestauranteResponseDTO;
import com.deliveryth.delivery_api.service.RestauranteService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/restaurantes")
public class RestauranteController {
    
    @Autowired
    private RestauranteService restauranteService;

    @PostMapping 
    public ResponseEntity<RestauranteResponseDTO> cadastrar(@RequestBody RestauranteDTO dto) {
    RestauranteResponseDTO novo = restauranteService.salve(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(novo);}

    @GetMapping("/{id}/taxa-entrega/{cep}") 
    public ResponseEntity<BigDecimal>calcularTaxa(@PathVariable Long id, @PathVariable String cep) {
        BigDecimal taxa = restauranteService.calcularFrete(id, cep);
        return ResponseEntity.ok(taxa); } 
    
    @GetMapping 
    public ResponseEntity<List<RestauranteResponseDTO>> listarDisponiveis() { 
        return ResponseEntity.ok(restauranteService.listarTodosAtivos()); }

    @GetMapping("/categoria/{categoria}") 
    public ResponseEntity<List<RestauranteResponseDTO>> buscarPorCategoria(@PathVariable String categoria) { 
        return ResponseEntity.ok(restauranteService.filtrarPorCategotia(categoria)); } 
        
    @PutMapping("/{id}") 
    public ResponseEntity<RestauranteResponseDTO> atualizar(@PathVariable Long id, @RequestBody RestauranteDTO dto) { 
        return ResponseEntity.ok(restauranteService.atualizarDados(id, dto)); }

    }