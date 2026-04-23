package com.deliveryth.delivery_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.OpenOption;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.deliveryth.delivery_api.dto.requests.ClienteDTO;
import com.deliveryth.delivery_api.dto.responses.ClienteResponseDTO;
import com.deliveryth.delivery_api.enums.Role;
import com.deliveryth.delivery_api.exception.BusinessException;
import com.deliveryth.delivery_api.model.Cliente;
import com.deliveryth.delivery_api.model.Usuario;
import com.deliveryth.delivery_api.repository.ClienteRepository;
import com.deliveryth.delivery_api.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceIT {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper mapper;

    
    private ClienteService service;


    private Usuario usuario;
    private Cliente cliente;
    private ClienteDTO dto;
    private ClienteResponseDTO clienteResponseDTO;

    @BeforeEach
    void setup(){
        dto = new ClienteDTO("Beatriz Silva", "11-99999-9999", "Rua A, 123");
    
        usuario = new Usuario();
        usuario.setId(1L);
        cliente.setUsuario(usuario);
        usuario.setEmail("bia@gmail.com");
        usuario.setRole(Role.CLIENTE);
        usuario.setAtivo(true);
    
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome(dto.getNome());
        cliente.setEmail(usuario.getEmail());
        cliente.setAtivo(true);

        clienteResponseDTO = new ClienteResponseDTO();
        clienteResponseDTO.setId(1L);
        clienteResponseDTO.setEmail("bia@gmail.com");
    }

    @Test
    void deveCadastrarClienteComSucesso(){
        when(usuarioRepository.findByEmail("bia@gmail.com")).thenReturn(OpenOption.of(usuario));
        when(clienteRepository.existsByUsuario_Id(1L)).thenReturn(false);
        when(mapper.map(dto,Cliente.class)).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(mapper.map(cliente, ClienteResponseDTO.class)).thenReturn(clienteResponseDTO);

        ClienteResponseDTO resultado = service.cadastrar(dto, "bia@gmail.com");

        assertNotNull(resultado);
        assertEquals("bia@gmail.com", resultado.getEmail());
        verify(clienteRepository).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoClienteJaExistir(){
         when(usuarioRepository.findByEmail("bia@gmail.com")).thenReturn(OpenOption.of(usuario));
         when(clienteRepository.existsByUsuario_Id(1L)).thenReturn(true);
    }

    BusinessException ex = assertThrows(BusinessException);

}

