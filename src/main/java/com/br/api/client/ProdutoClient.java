package com.br.api.client;

import com.br.api.endpoint.Endpoints;
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

    public static String getProdutoById(String id) {
        // Aqui você pode implementar a lógica para chamar a API e obter o produto pelo ID
        // Por exemplo, usando RestAssured ou outra biblioteca HTTP
        return "Produto com ID: " + id;
    }
}
