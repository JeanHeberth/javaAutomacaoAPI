package com.br.api.pedidos;

import com.br.api.client.PedidoClient;
import com.br.api.model.response.PagePedidoResponse;
import com.br.api.model.response.PedidoResponse;
import com.br.api.support.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Testes de GET /v1/pedidos.
 */
class ListarPedidosTest extends BaseTest {

    private static String token;

    @BeforeAll
    static void autenticar() {
        token = obterToken();
    }

    @Test
    @DisplayName("Listar pedidos sem token deve retornar 401")
    void listarPedidosSemTokenDeveRetornar401() {
        PedidoClient.listarSemToken(Collections.emptyMap())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Listar pedidos com token valido deve retornar 200 e uma pagina valida")
    void listarPedidosComTokenDeveRetornarPaginaValida() {
        PagePedidoResponse pagina = PedidoClient.listarPedidos(token, Collections.emptyMap())
                .then()
                .statusCode(200)
                .body("content", notNullValue())
                .body("number", equalTo(0))
                .body("size", equalTo(10))
                .extract()
                .as(PagePedidoResponse.class);

        assertThat(pagina.content()).isNotNull();
        assertThat(pagina.number()).isZero();
        assertThat(pagina.size()).isEqualTo(10);
        assertThat(pagina.numberOfElements()).isEqualTo(pagina.content().size());
        assertThat(pagina.totalElements()).isGreaterThanOrEqualTo(pagina.content().size());
    }

    @Test
    @DisplayName("Listar pedidos filtrando por status deve retornar apenas pedidos daquele status")
    void listarPedidosFiltrandoPorStatusDeveRespeitarFiltro() {
        PagePedidoResponse pagina = PedidoClient.listarPedidos(token, Map.of("status", "PENDENTE"))
                .then()
                .statusCode(200)
                .extract()
                .as(PagePedidoResponse.class);

        assertThat(pagina.content())
                .allSatisfy(pedido -> assertThat(pedido.status()).isEqualTo("PENDENTE"));
    }

    @Test
    @DisplayName("Listar pedidos respeitando parametros de paginacao (page e size)")
    void listarPedidosComPaginacaoCustomizadaDeveRespeitarParametros() {
        int tamanhoPagina = 5;

        PagePedidoResponse pagina = PedidoClient.listarPedidos(token, Map.of("page", 0, "size", tamanhoPagina))
                .then()
                .statusCode(200)
                .body("size", equalTo(tamanhoPagina))
                .body("number", equalTo(0))
                .extract()
                .as(PagePedidoResponse.class);

        assertThat(pagina.size()).isEqualTo(tamanhoPagina);
        assertThat(pagina.content().size()).isLessThanOrEqualTo(tamanhoPagina);
    }

    @Test
    @DisplayName("Cada pedido retornado deve ter um status dentro do fluxo valido")
    void pedidosRetornadosDevemTerStatusValido() {
        PagePedidoResponse pagina = PedidoClient.listarPedidos(token, Collections.emptyMap())
                .then()
                .statusCode(200)
                .extract()
                .as(PagePedidoResponse.class);

        Iterable<String> statusValidos = java.util.List.of(
                "PENDENTE", "CONFIRMADO", "EM_PREPARO", "ENVIADO", "ENTREGUE", "CANCELADO");

        for (PedidoResponse pedido : pagina.content()) {
            assertThat(pedido.status()).isIn(statusValidos);
        }
    }
}
