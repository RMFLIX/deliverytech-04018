package com.deliveryth.delivery_api.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deliveryth.delivery_api.dto.requests.ItemPedidoDTO;
import com.deliveryth.delivery_api.dto.requests.PedidoDTO;
import com.deliveryth.delivery_api.dto.responses.PedidoResponseDTO;
import com.deliveryth.delivery_api.exception.EstoqueInsuficienteException;
import com.deliveryth.delivery_api.model.Cliente;
import com.deliveryth.delivery_api.model.Pedido;
import com.deliveryth.delivery_api.model.Usuario;
import com.deliveryth.delivery_api.repository.ClienteRepository;
import com.deliveryth.delivery_api.repository.PedidoRepository;


@ExtendWith(MockitoExtension.class)
public class PedidoServiceIT {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private Usuario usuarioMock;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void criarPedido_ComProdutosValidos_DeveSalvarPedido() {
       
        PedidoDTO dto = new PedidoDTO();
        ItemPedidoDTO item = new ItemPedidoDTO();
        item.setProdutoId(1L);
        item.setQuantiade(100);
        dto.setItens(Arrays.asList(item));
        
        Cliente clienteAtivo = new Cliente();
        clienteAtivo.setAtivo(true);

        when(usuarioMock.getEmail()).thenReturn("teste@gmail.com");
        when(produtoService.validarEstoque(anyLong(), anyInt())).thenReturn(true);
        when(pedidoRepository.save(any())).thenReturn(new Pedido());
       
        when(clienteRepository.findByEmail(any())).thenReturn(Optional.of(clienteAtivo));

       PedidoResponseDTO resultado = pedidoService.criarPedido(dto, usuarioMock);
        assertNotNull(resultado);
        verify(pedidoRepository).save(any());

    }

    @Test
    void criarPedido_ComEstoqueInsuficiente_DeveLancarExcecao() {
        PedidoDTO dto = new PedidoDTO();
        ItemPedidoDTO item = new ItemPedidoDTO();
        dto.setClienteId(1L);
        item.setProdutoId(1L);
        item.setQuantiade(10);
        dto.setItens(Arrays.asList(item));

       
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.of(new Cliente()));
        when(produtoService.validarEstoque(anyLong(), anyInt())).thenReturn(false);

        assertThrows(EstoqueInsuficienteException.class, () -> {
            pedidoService.criarPedido(dto, usuarioMock);
        });
    }

     @Test
    void calcularTotal_DeveSomarProdutosCorretamente() {
         }
} 
