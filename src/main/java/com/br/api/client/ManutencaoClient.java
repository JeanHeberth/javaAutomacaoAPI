package com.br.api.client;

import io.restassured.response.Response;

import static com.br.api.endpoint.Endpoints.MANUTENCAO_BANCO;

/**
 * Chamada do endpoint de apoio de automacao (DELETE /v1/manutencao/banco),
 * que remove pedidos, produtos e usuarios do banco. Uso exclusivo para zerar
 * o estado entre execucoes de teste locais.
 */
public final class ManutencaoClient {

    private ManutencaoClient() {
    }

    public static Response limparBanco(String token) {
        return ApiClients.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(MANUTENCAO_BANCO.getPath());
    }
}
