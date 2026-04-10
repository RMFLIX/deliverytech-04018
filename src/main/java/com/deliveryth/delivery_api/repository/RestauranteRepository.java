package com.deliveryth.delivery_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliveryth.delivery_api.model.Restaurante;

import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

   List<Restaurante> findByCategoria(String categoria);
   List<Restaurante> findByAtivoTrue();
   
   }

