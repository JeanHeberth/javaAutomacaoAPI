package com.br.api.requests;

import com.br.api.dto.api.ProdutoRequest;
import io.restassured.response.Response;

import java.util.Collections;
import java.util.Map;

import static com.br.api.util.Endpoints.PRODUTOS;

public class ProdutoControllerRequests extends BaseController {

    public Response listarProdutos(String token, Map<String, ?> queryParams) {
        return get(PRODUTOS.getPath(), token, queryParams);
    }

    public Response listarProdutosSemToken(Map<String, ?> queryParams) {
        return get(PRODUTOS.getPath(), queryParams);
    }

    public Response criarProduto(String token, ProdutoRequest request) {
        return post(PRODUTOS.getPath(), token, request);
    }

    public Response criarProdutoSemToken(ProdutoRequest request) {
        return post(PRODUTOS.getPath(), request);
    }

    public Response buscarProdutoPorId(String token, Long id) {
        return buscarProdutoPorId(token, String.valueOf(id));
    }

    /**
     * Sobrecarga com id como String: permite exercitar valores que nao caberiam
     * em um Long (nao numerico, decimal, maior que Long.MAX_VALUE) nos testes
     * de validacao de id.
     */
    public Response buscarProdutoPorId(String token, String id) {
        return get(PRODUTOS.getPath() + "/" + id, token, Collections.emptyMap());
    }

    public Response excluirProduto(String token, Long id) {
        return excluirProduto(token, String.valueOf(id));
    }

    public Response excluirProduto(String token, String id) {
        return delete(PRODUTOS.getPath() + "/" + id, token);
    }
}
