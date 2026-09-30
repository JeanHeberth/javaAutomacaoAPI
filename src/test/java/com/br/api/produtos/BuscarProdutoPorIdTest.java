package com.br.api.produtos;

import com.br.api.dto.api.ProdutoRequest;
import com.br.api.dto.api.ProdutoResponse;
import com.br.api.requests.ProdutoControllerRequests;
import com.br.api.utils.BaseTest;
import com.br.api.utils.DataProvider;
import com.br.api.utils.ProdutoTestSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de GET /v1/produtos/{id}.
 */
public class BuscarProdutoPorIdTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BuscarProdutoPorIdTest.class);
    private static final List<Long> produtosCriados = new ArrayList<>();

    private final ProdutoControllerRequests produtoController = new ProdutoControllerRequests();

    private static String token;

    @BeforeClass
    public void autenticar() {
        token = obterToken();
    }

    @AfterClass
    public void limparProdutosCriados() {
        produtosCriados.forEach(id -> {
            int status = produtoController.excluirProduto(token, id).statusCode();
            log.info("Limpeza: produto id={} removido (HTTP {})", id, status);
        });
    }

    /**
     * O dataProvider (DataProvider#idsInvalidosProvider) cobre os dois
     * comportamentos observados na API: id com formato valido de Long mas
     * inexistente/fora do dominio (404) e id com formato invalido para o
     * path variable (400).
     */
    @Test(dataProvider = "idsInvalidosProvider", dataProviderClass = DataProvider.class,
            description = "Buscar produto com id invalido deve retornar o status esperado")
    public void buscarProdutoComIdInvalidoDeveRetornarStatusEsperado(String descricao, String id, int statusEsperado) {
        produtoController.buscarProdutoPorId(token, id)
                .then()
                .statusCode(statusEsperado);
    }

    /**
     * O dataProvider (DataProvider#nomeProdutoMassaProvider) le o nome
     * cadastrado em massa/produtos.yaml; o produto e criado a partir dessa
     * mesma massa (ProdutoTestSupport.produtoValido), entao o nome devolvido
     * pela API deve iniciar com o nome da massa (o sufixo unico garante a
     * unicidade exigida pela API).
     */
    @Test(dataProvider = "nomeProdutoMassaProvider", dataProviderClass = DataProvider.class,
            description = "Buscar produto por id deve retornar o nome informado na massa de produtos")
    public void buscarProdutoPorIdDeveRetornarNomeDaMassa(String nomeEsperado) {
        ProdutoRequest request = ProdutoTestSupport.produtoValido();

        ProdutoResponse produtoCriado = produtoController.criarProduto(token, request)
                .then()
                .statusCode(201)
                .extract()
                .as(ProdutoResponse.class);
        produtosCriados.add(produtoCriado.id());

        ProdutoResponse produtoBuscado = produtoController.buscarProdutoPorId(token, produtoCriado.id())
                .then()
                .statusCode(200)
                .extract()
                .as(ProdutoResponse.class);

        assertThat(produtoBuscado.nome()).startsWith(nomeEsperado);
    }
}
