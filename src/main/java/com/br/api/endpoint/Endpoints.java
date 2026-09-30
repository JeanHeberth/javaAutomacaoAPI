package com.br.api.endpoint;

import lombok.Getter;

@Getter
public enum Endpoints {

    HEALTH("actuator/health"),
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
