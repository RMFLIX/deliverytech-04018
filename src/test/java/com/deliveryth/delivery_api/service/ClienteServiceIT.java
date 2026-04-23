package com.deliveryth.delivery_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Optional;

import org.h2.mvstore.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.deliveryth.delivery_api.dto.requests.ClienteDTO;
import com.deliveryth.delivery_api.dto.responses.ClienteResponseDTO;
import com.deliveryth.delivery_api.enums.Role;
import com.deliveryth.delivery_api.exception.BusinessException;
import com.deliveryth.delivery_api.exception.EntityNotFoundException;
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
    

    BusinessException ex = assertThrows(BusinessException.class, ()-> {
    });

    assertEquals("Cliente já cadastrado para este usuário.", ex.getMessage());

    }

    @Test
    void deveListarClientesAtivos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Cliente> page = new PageImpl<>(List.of(cliente));

        when(clienteRepository.findByAtivoTrue(pageable)).thenReturn(page);
        when(mapper.map(any(Cliente.class), eq(ClienteResponseDTO.class))).thenReturn();

        Page<ClienteResponseDTO> resultado = service.listarAtivos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(mapper, atLeastOnce()).map(any(Cliente.class), eq(ClienteResponseDTO.class));
    }

    @Test
    void deveBuscarClientePorIdComSucesso(){
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mapper.map(cliente, ClienteResponseDTO.class)).thenReturn(clienteResponseDTO);

        ClienteResponseDTO resultado = service.buscarPorId(1L);
        assertNotNull(resultado);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado(){
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
       
        assertThrows(EntityNotFoundException.class, ()->{
            service.buscarPorId(1L);
        });
    }

    @Test
    void deveInativarOuAtivarCliente(){
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any())).thenReturn(cliente);
        when(mapper.map(cliente, ClienteResponseDTO.class)).thenReturn(clienteResponseDTO);

        ClienteResponseDTO resultado = service.inativar(1L);

        assertNotNull(resultado);
        verify(clienteRepository).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoEncontradoEmInativar(){
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()->{
            service.inativar(1L);
        });
    }
    
}


