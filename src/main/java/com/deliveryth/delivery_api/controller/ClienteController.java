package com.deliveryth.delivery_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliveryth.delivery_api.dto.requests.ClienteDTO;
import com.deliveryth.delivery_api.dto.responses.ClienteResponseDTO;
import com.deliveryth.delivery_api.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
@Tag(name="Clientes", description ="Endpoints para gerenciamento de clientes.")
public class ClienteController {
    private ClienteService service;

    public ClienteController ( ClienteService service){
        this.service = service;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ClienteResponseDTO> cadastrar(@Valid @RequestBody ClienteDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @Operation(summary ="Listar clientes ativos.")
    @ApiResponses(
        value ={
            @ApiResponse(responseCode="200", description ="Lista de clientes ativos retornados.")
            
        }
    )
    @GetMapping
    public List<ClienteResponseDTO> listarAtivos(){
        return service.listarAtivos();
    }
    
    @GetMapping("/{id}")
    public ClienteResponseDTO buscarPorId(@PathVariable Long id){
       return service.buscarPorId(id);
    }

    @PutMapping("/{id}/inativar-cliente")
    public ClienteResponseDTO inativar(@PathVariable Long id){
        return service.inativar(id);
    }

    /*@PutMapping("/{id}/atualizar-dados-clientes")
    public Cliente atualizar(@PathVariable Long id, @RequestBody Cliente dados){
        return service.atualizar(id, dados);
    }*/
}








