package com.br.api.client;

import io.restassured.response.Response;

import java.util.Map;

import static com.br.api.endpoint.Endpoints.*;

/**
 * Chamadas HTTP do endpoint de listagem de pedidos (GET /v1/pedidos).
 */
public final class PedidoClient {

    private PedidoClient() {
    }

    public static Response listarPedidos(String token, Map<String, ?> queryParams) {
        return ApiClients.given()
                .header("Authorization", "Bearer " + token)
                .queryParams(queryParams)
                .when()
                .get(PEDIDO.getPath());
    }

    public static Response listarSemToken(Map<String, ?> queryParams) {
        return ApiClients.given()
                .queryParams(queryParams)
                .when()
                .get(PEDIDO.getPath());
    }
}
