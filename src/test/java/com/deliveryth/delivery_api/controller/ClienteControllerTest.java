package com.deliveryth.delivery_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.deliveryth.delivery_api.config.SecurityTestConfig;
import com.deliveryth.delivery_api.dto.requests.ClienteDTO;
import com.deliveryth.delivery_api.dto.responses.ClienteResponseDTO;
import com.deliveryth.delivery_api.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(
    controllers = ClienteController.class
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSGNABLE_TYPE
    )
)
@Import(SecurityTestConfig.class)
public class ClienteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService service;


    private ClienteDTO clienteValido(){
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("João Silva");
        dto.setTelefone("(11)99999-9999");
        dto.setEndereco("Rua A, 123");
        return dto;
    }

    @Test
    @WithMockUser
    void deveCadastrarCliente() throws Exception{
        ClienteDTO dto = clienteValido();
       
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setNome("João Silva");

        when(service.cadastrar(any(), eq("teste@gmail.com")))
             .thenReturn(response);


             mockMvc.perform(pots("/api/clientes/cadastrar")
             .contentType(MediaType.APPLICATION_JSON)
             .Content(objectMapper.writeValueAsString(dto))
             .andExpect(status().isCreated())
             .andExpect(jsonPath("$.nome").value("João Silva")));
    }


}
