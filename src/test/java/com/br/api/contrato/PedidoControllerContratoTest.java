package com.br.api.contrato;

import com.br.api.dto.api.PageResponse;
import com.br.api.dto.api.PedidoResponse;
import com.br.api.requests.PedidoControllerRequests;
import com.br.api.utils.BaseTest;
import com.br.api.utils.JsonSchemaGenerator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

/**
 * Teste de contrato (JSON Schema) do endpoint de pedidos, com o schema
 * gerado a partir do DTO real via JsonSchemaGenerator.
 */
public class PedidoControllerContratoTest extends BaseTest {

    private final PedidoControllerRequests pedidoController = new PedidoControllerRequests();

    private static String token;

    @BeforeClass
    public void autenticar() {
        token = obterToken();
    }

    @Test(description = "Contrato de GET /pedidos deve respeitar o schema de PageResponse<PedidoResponse>")
    public void validarContratoDeListarPedidos() {
        String jsonSchema = JsonSchemaGenerator.generate(PageResponse.class, PedidoResponse.class);

        pedidoController.listarPedidos(token, Collections.emptyMap())
                .then()
                .statusCode(200)
                .body(matchesJsonSchema(jsonSchema));
    }
}
