package com.deliveryth.delivery_api.service;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliveryth.delivery_api.dto.requests.ItemPedidoDTO;
import com.deliveryth.delivery_api.dto.requests.PedidoDTO;
import com.deliveryth.delivery_api.dto.responses.PedidoResponseDTO;
import com.deliveryth.delivery_api.enums.StatusPedido;
import com.deliveryth.delivery_api.exception.BusinessException;
import com.deliveryth.delivery_api.exception.EntityNotFoundException;
import com.deliveryth.delivery_api.model.Cliente;
import com.deliveryth.delivery_api.model.ItemPedido;
import com.deliveryth.delivery_api.model.Pedido;
import com.deliveryth.delivery_api.model.Produto;
import com.deliveryth.delivery_api.model.Restaurante;
import com.deliveryth.delivery_api.model.Usuario;
import com.deliveryth.delivery_api.repository.ClienteRepository;
import com.deliveryth.delivery_api.repository.ItemPedidoRepository;
import com.deliveryth.delivery_api.repository.PedidoRepository;
import com.deliveryth.delivery_api.repository.ProdutoRepository;
import com.deliveryth.delivery_api.repository.RestauranteRepository;


@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;
    
    private final ModelMapper mapper;


    private PedidoResponseDTO toResponseDTO(Pedido pedido){
        return mapper.map(pedido, PedidoResponseDTO.class);
    }
    
    public PedidoService(PedidoRepository pedidoRepository,
         ClienteRepository clienteRepository,
         RestauranteRepository restauranteRepository,
         ItemPedidoRepository itemPedidoRepository,
         ProdutoRepository produtoRepository,
         ModelMapper mapper){
            
            this.pedidoRepository = pedidoRepository;
            this.clienteRepository = clienteRepository;
            this.restauranteRepository = restauranteRepository;
            this.itemPedidoRepository = itemPedidoRepository;
            this.produtoRepository = produtoRepository;
            this.mapper = mapper;
        }

        private PedidoResponseDTO toDTO(Pedido pedido){
            return mapper.map(pedido, PedidoResponseDTO.class);
        }

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoDTO dto, Usuario usuarioLogado){

        if (usuarioLogado == null){
            throw new BusinessException("Usuário não autenticado.");
        }
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail())
                 .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado para este usuário."));

        if (!cliente.isAtivo()) {
            throw new BusinessException("Cliente inativo.");
        }

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                 .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));

        if (!restaurante.isAtivo()) 
            throw new BusinessException("Restaurante inativo.");

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoDTO itemDTO : dto.getItens()){
           
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
            .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

            if (!produto.isDisponivel()) 
                throw new BusinessException("Produto indisponível: " + produto.getNome());

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantiade());
            item.setPrecoUnitario(produto.getPreco());

            BigDecimal subtotal = produto.getPreco()
                    .multiply(BigDecimal.valueOf(itemDTO.getQuantiade()));

            item.setSubtotal(subtotal);

            pedido.getItens().add(item);
            total = total.add(subtotal);

        }

        pedido.setValorTotal(total);

        return toResponseDTO(pedidoRepository.save(pedido));
    }

    private void validarDonoPedido(Pedido pedido, Usuario usuarioLogado){
        if (!pedido.getCliente().getEmail().equals(usuarioLogado.getEmail())){
            throw new BusinessException("Você não tem permissão para acessar este pedido.");
        }
    }

    @Transactional
    public PedidoResponseDTO confirmarPedido(Long pedidoId, Usuario usuarioLogado){

        Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado.") );

        validarDonoPedido(pedido, usuarioLogado);

        if(pedido.getStatus() != StatusPedido.PENDENTE){
            throw new BusinessException("Apenas pedidos PENDENTES podem ser confirmados.");
        }

        pedido.setStatus(StatusPedido.CONFIRMADO);
        return toResponseDTO(pedido);
    }

    @Transactional
    @CacheEvict(value = "pedidos", key = "#pedidoId")
    public PedidoResponseDTO atualizarStatus(Long pedidoId, Usuario usuarioLogado){

        Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(()-> new EntityNotFoundException("Pedido não encontrado."));

        StatusPedido statusPedido = pedido.getStatus();
        
        switch (statusPedido) {
            case CONFIRMADO -> pedido.setStatus(StatusPedido.PREPARANDO);
            case PREPARANDO -> pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);
            case SAIU_PARA_ENTREGA -> pedido.setStatus(StatusPedido.ENTREGUE);
                
            case CANCELADO, ENTREGUE ->
                 throw new BusinessException("Status do Pedido não pode mais ser avançado.");
            default ->
                throw new BusinessException("Status é inválido para avanço.");
        }
        return toResponseDTO(pedido);
    }

    public Page<PedidoResponseDTO> listarPorCliente(Long clienteId, Pageable pageable){
        return pedidoRepository.buscarItensPorClientes(clienteId, pageable)
        .map(this:: toResponseDTO);
    }

    @Transactional
    public PedidoResponseDTO cancelarPedido(Long pedidoId, Usuario usuarioLogado){
        Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        if(pedido.getStatus() == StatusPedido.ENTREGUE){
            throw new BusinessException("Pedido entregue não pode ser cancelado.");
        }

        pedido.setStatus(StatusPedido.CANCELADO);

        Pedido salvo = pedidoRepository.save(pedido);
        return toResponseDTO(salvo);
    }

    @Transactional
    public Page<PedidoResponseDTO> meusPedidos(Usuario usuarioLogado, Pageable pageable){

        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail())
        .orElseThrow(() -> new BusinessException("Cliente não encontrado."));

        return pedidoRepository.buscarItensPorClientes(cliente.getId(), pageable)
        .map(this::toDTO);
    }

    @Transactional
    @Cacheable(value = "pedidos", key = "#id")
    public PedidoResponseDTO buscarPorId(Long pedidoId) {
   
        Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        return toResponseDTO(pedido);
}

    /* public ItemPedido adicionarItem(Long pedidoId, Long produtoId, Integer quantidade){ 
        Pedido pedido = pedidoRepository.findById(pedidoId) 
        .orElseThrow(()-> new IllegalArgumentException("Pedido não encontrado.")); 
        
        Produto produto = produtoRepository.findById(produtoId) 
        .orElseThrow(()-> new IllegalArgumentException("Produto não encontrado.")); 
        
        ItemPedido item = new ItemPedido(); 
        item.setPedido(pedido); 
        item.setProduto(produto); 
        item.setQuantidade(quantidade); 
        item.setPrecoUnitario(produto.getPreco()); 
        
        BigDecimal subtotal = produto.getPreco() 
          .multiply(BigDecimal.valueOf(quantidade)); 
        item.setSubtotal(subtotal); 
        itemPedidoRepository.save(item); 
        
        pedido.setValorTotal(pedido.getValorTotal().add(subtotal)); 
        pedidoRepository.save(pedido); return item;

        public void processarPedido(){
        Span novoSpan = trace.nextSpan()
        }

*/
}