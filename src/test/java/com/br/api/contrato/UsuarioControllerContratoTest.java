package com.br.api.contrato;

import com.br.api.dto.api.UsuarioResponse;
import com.br.api.requests.UsuarioControllerRequests;
import com.br.api.utils.BaseTest;
import com.br.api.utils.JsonSchemaGenerator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

/**
 * Testes de contrato (JSON Schema) do endpoint de usuarios, com o schema
 * gerado a partir dos DTOs reais via JsonSchemaGenerator.
 */
public class UsuarioControllerContratoTest extends BaseTest {

    private final UsuarioControllerRequests usuarioController = new UsuarioControllerRequests();

    private static String token;

    @BeforeClass
    public void autenticar() {
        token = obterToken();
    }

    @Test(description = "Contrato de GET /usuarios deve respeitar o schema de uma lista de UsuarioResponse")
    public void validarContratoDeListarUsuarios() {
        String jsonSchema = JsonSchemaGenerator.generate(List.class, UsuarioResponse.class);

        usuarioController.listarUsuarios(token, Collections.emptyMap())
                .then()
                .statusCode(200)
                .body(matchesJsonSchema(jsonSchema));
    }
}
