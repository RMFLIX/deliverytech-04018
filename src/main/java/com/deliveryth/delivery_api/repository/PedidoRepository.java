package com.deliveryth.delivery_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliveryth.delivery_api.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {}
