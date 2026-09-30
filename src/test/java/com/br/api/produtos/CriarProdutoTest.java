package com.br.api.produtos;

import com.br.api.dto.api.ProdutoRequest;
import com.br.api.dto.api.ProdutoResponse;
import com.br.api.requests.ProdutoControllerRequests;
import com.br.api.utils.BaseTest;
import com.br.api.utils.ProdutoTestSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de POST /v1/produtos (criar_1 no swagger).
 *
 * Todo produto criado com sucesso e registrado em {@code produtosCriados} e
 * removido no @AfterClass, para nao deixar sujeira no banco entre execucoes.
 */
public class CriarProdutoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CriarProdutoTest.class);
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

    @Test(description = "Criar produto com dados validos e token deve retornar 201")
    public void criarProdutoComDadosValidosDeveRetornar201() {
        ProdutoRequest request = ProdutoTestSupport.produtoValido();

        ProdutoResponse produto = produtoController.criarProduto(token, request)
                .then()
                .statusCode(201)
                .extract()
                .as(ProdutoResponse.class);
        produtosCriados.add(produto.id());

        assertThat(produto.id()).isNotNull();
        assertThat(produto.nome()).isEqualTo(request.nome());
        assertThat(produto.descricao()).isEqualTo(request.descricao());
        assertThat(produto.preco()).isEqualByComparingTo(request.preco());
        assertThat(produto.estoque()).isEqualTo(request.estoque());
        assertThat(produto.categoria()).isEqualTo(request.categoria());
        assertThat(produto.ativo()).isTrue();
    }

    @Test(description = "Criar produto sem token deve retornar 401")
    public void criarProdutoSemTokenDeveRetornar401() {
        produtoController.criarProdutoSemToken(ProdutoTestSupport.produtoValido())
                .then()
                .statusCode(401);
    }

    @Test(dataProvider = "payloadsInvalidos", description = "Criar produto com dados invalidos deve retornar 400")
    public void criarProdutoComDadosInvalidosDeveRetornar400(String descricaoCaso, ProdutoRequest request) {
        produtoController.criarProduto(token, request)
                .then()
                .statusCode(400);
    }

    @DataProvider(name = "payloadsInvalidos")
    public Object[][] payloadsInvalidos() {
        return new Object[][]{
                {"Nome em branco", new ProdutoRequest("", "Descricao valida", new BigDecimal("10.00"), 1, "ELETRONICO")},
                {"Preco zero", new ProdutoRequest(ProdutoTestSupport.nomeUnico("Produto"), "Descricao valida", BigDecimal.ZERO, 1, "ELETRONICO")},
                {"Estoque negativo", new ProdutoRequest(ProdutoTestSupport.nomeUnico("Produto"), "Descricao valida", new BigDecimal("10.00"), -1, "ELETRONICO")},
                {"Categoria invalida", new ProdutoRequest(ProdutoTestSupport.nomeUnico("Produto"), "Descricao valida", new BigDecimal("10.00"), 1, "CATEGORIA_INEXISTENTE")}
        };
    }

    @Test(description = "Criar produto com nome ja cadastrado deve retornar 409")
    public void criarProdutoComNomeDuplicadoDeveRetornar409() {
        ProdutoRequest request = ProdutoTestSupport.produtoValido();

        Long idCriado = produtoController.criarProduto(token, request)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");
        produtosCriados.add(idCriado);

        produtoController.criarProduto(token, request)
                .then()
                .statusCode(409);
    }
}
