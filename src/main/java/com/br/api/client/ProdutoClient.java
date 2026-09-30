package com.br.api.client;

import com.br.api.endpoint.Endpoints;
import com.br.api.model.request.ProdutoRequest;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;

import java.util.Map;

import static com.br.api.endpoint.Endpoints.*;

@AllArgsConstructor
public class ProdutoClient {


    public static Response listarProdutos(String token, Map<String, ?> queryParams) {
        return ApiClients.given()
                .header("Authorization", "Bearer " + token)
                .queryParams(queryParams)
                .when()
                .get(PRODUTOS.getPath());
    }

    public static Response listarProdutosSemToken(Map<String, ?> queryParams) {
        return ApiClients.given()
                .queryParams(queryParams)
                .when()
                .get(PRODUTOS.getPath());
    }

    public static Response criarProduto(String token, ProdutoRequest request) {
        return ApiClients.given()
                .header("Authorization", "Bearer " + token)
                .body(request)
                .when()
                .post(PRODUTOS.getPath());
    }

    public static Response criarProdutoSemToken(ProdutoRequest request) {
        return ApiClients.given()
                .body(request)
                .when()
                .post(PRODUTOS.getPath());
    }

    public static Response excluirProduto(String token, Long id) {
        return ApiClients.given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(PRODUTOS.getPath() + "/" + id);
    }

}
