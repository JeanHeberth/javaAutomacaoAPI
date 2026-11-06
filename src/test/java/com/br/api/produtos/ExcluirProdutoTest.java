package com.br.api.produtos;

import com.br.api.requests.ProdutoControllerRequests;
import com.br.api.utils.BaseTest;
import com.br.api.utils.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Testes de DELETE /v1/produtos/{id} com ids invalidos.
 *
 * O dataProvider (DataProvider#idsInvalidosProvider) cobre os dois
 * comportamentos observados na API: id com formato valido de Long mas
 * inexistente/fora do dominio (404) e id com formato invalido para o path
 * variable (400).
 */
public class ExcluirProdutoTest extends BaseTest {

    private final ProdutoControllerRequests produtoController = new ProdutoControllerRequests();

    private static String token;

    @BeforeClass
    public void autenticar() {
        token = obterToken();
    }

    @Test(dataProvider = "idsInvalidosProvider", dataProviderClass = DataProvider.class,
            description = "Excluir produto com id invalido deve retornar o status esperado")
    public void excluirProdutoComIdInvalidoDeveRetornarStatusEsperado(String descricao, String id, int statusEsperado) {
        produtoController.excluirProduto(token, id)
                .then()
                .statusCode(statusEsperado);
    }
}
