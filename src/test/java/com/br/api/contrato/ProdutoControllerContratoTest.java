package com.br.api.contrato;

import com.br.api.dto.api.PageResponse;
import com.br.api.dto.api.ProdutoRequest;
import com.br.api.dto.api.ProdutoResponse;
import com.br.api.requests.ProdutoControllerRequests;
import com.br.api.utils.BaseTest;
import com.br.api.utils.JsonSchemaGenerator;
import com.br.api.utils.ProdutoTestSupport;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

/**
 * Testes de contrato (JSON Schema) dos endpoints de produto, com o schema
 * gerado a partir dos DTOs reais via JsonSchemaGenerator.
 */
public class ProdutoControllerContratoTest extends BaseTest {

    private final ProdutoControllerRequests produtoController = new ProdutoControllerRequests();

    private static final List<Long> produtosCriados = new ArrayList<>();
    private static String token;

    @BeforeClass
    public void autenticar() {
        token = obterToken();
    }

    @AfterClass
    public void limparProdutosCriados() {
        produtosCriados.forEach(id -> produtoController.excluirProduto(token, id));
    }

    @Test(description = "Contrato de GET /produtos deve respeitar o schema de PageResponse<ProdutoResponse>")
    public void validarContratoDeListarProdutos() {
        String jsonSchema = JsonSchemaGenerator.generate(PageResponse.class, ProdutoResponse.class);

        produtoController.listarProdutos(token, Collections.emptyMap())
                .then()
                .statusCode(200)
                .body(matchesJsonSchema(jsonSchema));
    }

    @Test(description = "Contrato de POST /produtos deve respeitar o schema de ProdutoResponse")
    public void validarContratoDeCriarProduto() {
        ProdutoRequest request = ProdutoTestSupport.produtoValido();
        String jsonSchema = JsonSchemaGenerator.generate(ProdutoResponse.class);

        ProdutoResponse produto = produtoController.criarProduto(token, request)
                .then()
                .statusCode(201)
                .body(matchesJsonSchema(jsonSchema))
                .extract()
                .as(ProdutoResponse.class);
        produtosCriados.add(produto.id());
    }
}
