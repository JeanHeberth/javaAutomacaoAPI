package com.br.api.pedidos;

import com.br.api.dto.api.PageResponse;
import com.br.api.dto.api.PedidoResponse;
import com.br.api.requests.PedidoControllerRequests;
import com.br.api.utils.BaseTest;
import io.restassured.common.mapper.TypeRef;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Testes de GET /v1/pedidos.
 */
public class ListarPedidosTest extends BaseTest {

    private final PedidoControllerRequests pedidoController = new PedidoControllerRequests();

    private static String token;

    @BeforeClass
    public void autenticar() {
        token = obterToken();
    }

    @Test(description = "Listar pedidos sem token deve retornar 401")
    public void listarPedidosSemTokenDeveRetornar401() {
        pedidoController.listarSemToken(Collections.emptyMap())
                .then()
                .statusCode(401);
    }

    @Test(description = "Listar pedidos com token valido deve retornar 200 e uma pagina valida")
    public void listarPedidosComTokenDeveRetornarPaginaValida() {
        PageResponse<PedidoResponse> pagina = pedidoController.listarPedidos(token, Collections.emptyMap())
                .then()
                .statusCode(200)
                .body("content", notNullValue())
                .body("number", equalTo(0))
                .body("size", equalTo(10))
                .extract()
                .as(new TypeRef<PageResponse<PedidoResponse>>() { });

        assertThat(pagina.content()).isNotNull();
        assertThat(pagina.number()).isZero();
        assertThat(pagina.size()).isEqualTo(10);
        assertThat(pagina.numberOfElements()).isEqualTo(pagina.content().size());
        assertThat(pagina.totalElements()).isGreaterThanOrEqualTo(pagina.content().size());
    }

    @Test(description = "Listar pedidos filtrando por status deve retornar apenas pedidos daquele status")
    public void listarPedidosFiltrandoPorStatusDeveRespeitarFiltro() {
        PageResponse<PedidoResponse> pagina = pedidoController.listarPedidos(token, Map.of("status", "PENDENTE"))
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<PageResponse<PedidoResponse>>() { });

        assertThat(pagina.content())
                .allSatisfy(pedido -> assertThat(pedido.status()).isEqualTo("PENDENTE"));
    }

    @Test(description = "Listar pedidos respeitando parametros de paginacao (page e size)")
    public void listarPedidosComPaginacaoCustomizadaDeveRespeitarParametros() {
        int tamanhoPagina = 5;

        PageResponse<PedidoResponse> pagina = pedidoController.listarPedidos(token, Map.of("page", 0, "size", tamanhoPagina))
                .then()
                .statusCode(200)
                .body("size", equalTo(tamanhoPagina))
                .body("number", equalTo(0))
                .extract()
                .as(new TypeRef<PageResponse<PedidoResponse>>() { });

        assertThat(pagina.size()).isEqualTo(tamanhoPagina);
        assertThat(pagina.content().size()).isLessThanOrEqualTo(tamanhoPagina);
    }

    @Test(description = "Cada pedido retornado deve ter um status dentro do fluxo valido")
    public void pedidosRetornadosDevemTerStatusValido() {
        PageResponse<PedidoResponse> pagina = pedidoController.listarPedidos(token, Collections.emptyMap())
                .then()
                .statusCode(200)
                .extract()
                .as(new TypeRef<PageResponse<PedidoResponse>>() { });

        Iterable<String> statusValidos = java.util.List.of(
                "PENDENTE", "CONFIRMADO", "EM_PREPARO", "ENVIADO", "ENTREGUE", "CANCELADO");

        for (PedidoResponse pedido : pagina.content()) {
            assertThat(pedido.status()).isIn(statusValidos);
        }
    }
}
