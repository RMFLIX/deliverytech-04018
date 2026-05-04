package com.deliveryth.delivery_api.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.deliveryth.delivery_api.model.Usuario;
import com.deliveryth.delivery_api.service.PedidoService;

@SpringBootTest
public class PedidoCacheTest {

    @Autowired
    private PedidoService pedidoService;

    @Test
    public void validarFuncionamentoDoCache() {
        
        Long idExistente = 1L; 

        Usuario usuarioLogado = new Usuario();

        System.out.println("--- PRIMEIRA CHAMADA (DEVE IR NO BANCO) ---");
        pedidoService.buscarPorId(idExistente);

        System.out.println("--- SEGUNDA CHAMADA (NÃO DEVE GERAR SQL - VEM DO CACHE) ---");
        pedidoService.buscarPorId(idExistente);

        System.out.println("--- ATUALIZANDO STATUS (LIMPA O CACHE) ---");
        
        pedidoService.atualizarStatus(idExistente, usuarioLogado); 

        System.out.println("--- TERCEIRA CHAMADA (DEVE IR NO BANCO DE NOVO) ---");
        pedidoService.buscarPorId(idExistente);
    }
}
