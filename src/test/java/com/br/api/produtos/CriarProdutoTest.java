package com.br.api.produtos;

import com.br.api.client.ProdutoClient;
import com.br.api.model.request.ProdutoRequest;
import com.br.api.model.response.ProdutoResponse;
import com.br.api.support.BaseTest;
import com.br.api.support.ProdutoTestSupport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de POST /v1/produtos (criar_1 no swagger).
 *
 * Todo produto criado com sucesso e registrado em {@code produtosCriados} e
 * removido no @AfterAll, para nao deixar sujeira no banco entre execucoes.
 */
class CriarProdutoTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CriarProdutoTest.class);
    private static final List<Long> produtosCriados = new ArrayList<>();

    private static String token;

    @BeforeAll
    static void autenticar() {
        token = obterToken();
    }

    @AfterAll
    static void limparProdutosCriados() {
        produtosCriados.forEach(id -> {
            int status = ProdutoClient.excluirProduto(token, id).statusCode();
            log.info("Limpeza: produto id={} removido (HTTP {})", id, status);
        });
    }

    @Test
    @DisplayName("Criar produto com dados validos e token deve retornar 201")
    void criarProdutoComDadosValidosDeveRetornar201() {
        ProdutoRequest request = ProdutoTestSupport.produtoValido();

        ProdutoResponse produto = ProdutoClient.criarProduto(token, request)
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

    @Test
    @DisplayName("Criar produto sem token deve retornar 401")
    void criarProdutoSemTokenDeveRetornar401() {
        ProdutoClient.criarProdutoSemToken(ProdutoTestSupport.produtoValido())
                .then()
                .statusCode(401);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("payloadsInvalidos")
    @DisplayName("Criar produto com dados invalidos deve retornar 400")
    void criarProdutoComDadosInvalidosDeveRetornar400(String descricaoCaso, ProdutoRequest request) {
        ProdutoClient.criarProduto(token, request)
                .then()
                .statusCode(400);
    }

    static Stream<Arguments> payloadsInvalidos() {
        return Stream.of(
                Arguments.of("Nome em branco", new ProdutoRequest("", "Descricao valida", new BigDecimal("10.00"), 1, "ELETRONICO")),
                Arguments.of("Preco zero", new ProdutoRequest(ProdutoTestSupport.nomeUnico("Produto"), "Descricao valida", BigDecimal.ZERO, 1, "ELETRONICO")),
                Arguments.of("Estoque negativo", new ProdutoRequest(ProdutoTestSupport.nomeUnico("Produto"), "Descricao valida", new BigDecimal("10.00"), -1, "ELETRONICO")),
                Arguments.of("Categoria invalida", new ProdutoRequest(ProdutoTestSupport.nomeUnico("Produto"), "Descricao valida", new BigDecimal("10.00"), 1, "CATEGORIA_INEXISTENTE"))
        );
    }

    @Test
    @DisplayName("Criar produto com nome ja cadastrado deve retornar 409")
    void criarProdutoComNomeDuplicadoDeveRetornar409() {
        ProdutoRequest request = ProdutoTestSupport.produtoValido();

        Long idCriado = ProdutoClient.criarProduto(token, request)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");
        produtosCriados.add(idCriado);

        ProdutoClient.criarProduto(token, request)
                .then()
                .statusCode(409);
    }
}
