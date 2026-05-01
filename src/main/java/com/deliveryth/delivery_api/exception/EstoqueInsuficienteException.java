package com.deliveryth.delivery_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EstoqueInsuficienteException extends RuntimeException {

    public EstoqueInsuficienteException() {
        super("Estoque insuficiente para realizar o pedido.");
    }

    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
