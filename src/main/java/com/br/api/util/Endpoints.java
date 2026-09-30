package com.br.api.util;

import lombok.Getter;

@Getter
public enum Endpoints {

    PEDIDO("/pedidos"),
    PRODUTOS("/produtos"),
    LOGIN("/auth/login"),
    REGISTRO("/auth/registro"),
    USUARIOS("/usuarios"),
    MANUTENCAO_BANCO("/manutencao/banco");

    private final String path;

    Endpoints(String path) {
        this.path = path;
    }

}
