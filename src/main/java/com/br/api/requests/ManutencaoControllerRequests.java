package com.br.api.requests;

import io.restassured.response.Response;

import static com.br.api.util.Endpoints.MANUTENCAO_BANCO;

/**
 * Chamada do endpoint de apoio de automacao (DELETE /v1/manutencao/banco),
 * que remove pedidos, produtos e usuarios do banco. Uso exclusivo para zerar
 * o estado entre execucoes de teste locais.
 */
public class ManutencaoControllerRequests extends BaseController {

    public Response limparBanco(String token) {
        return delete(MANUTENCAO_BANCO.getPath(), token);
    }
}
