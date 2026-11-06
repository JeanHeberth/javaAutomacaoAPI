package com.br.api.requests;

import io.restassured.response.Response;

import java.util.Map;

import static com.br.api.util.Endpoints.PEDIDO;

/**
 * Chamadas HTTP do endpoint de listagem de pedidos (GET /v1/pedidos).
 */
public class PedidoControllerRequests extends BaseController {

    public Response listarPedidos(String token, Map<String, ?> queryParams) {
        return get(PEDIDO.getPath(), token, queryParams);
    }

    public Response listarSemToken(Map<String, ?> queryParams) {
        return get(PEDIDO.getPath(), queryParams);
    }
}
