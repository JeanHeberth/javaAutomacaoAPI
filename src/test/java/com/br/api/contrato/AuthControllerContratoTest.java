package com.br.api.contrato;

import com.br.api.dto.auth.AuthResponse;
import com.br.api.dto.auth.LoginRequest;
import com.br.api.dto.auth.RegistroRequest;
import com.br.api.dto.massa.MassaUsuarios;
import com.br.api.dto.massa.UsuarioMassa;
import com.br.api.requests.AuthControllerRequests;
import com.br.api.requests.UsuarioControllerRequests;
import com.br.api.util.ConfigLoader;
import com.br.api.util.MassaArquivos;
import com.br.api.utils.BaseTest;
import com.br.api.utils.JsonSchemaGenerator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

/**
 * Testes de contrato (JSON Schema) dos endpoints de autenticacao, com o
 * schema gerado a partir dos DTOs reais via JsonSchemaGenerator.
 */
public class AuthControllerContratoTest extends BaseTest {

    private final AuthControllerRequests authController = new AuthControllerRequests();
    private final UsuarioControllerRequests usuarioController = new UsuarioControllerRequests();

    private static final List<Long> usuariosCriados = new ArrayList<>();
    private static String token;

    @BeforeClass
    public void autenticar() {
        token = obterToken();
    }

    @AfterClass
    public void limparUsuariosCriados() {
        usuariosCriados.forEach(id -> usuarioController.excluirUsuario(token, id));
    }

    @Test(description = "Contrato de POST /auth/login deve respeitar o schema de AuthResponse")
    public void validarContratoDeLogin() {
        MassaUsuarios massa = ConfigLoader.carregar(MassaArquivos.USUARIOS.getPath(), MassaUsuarios.class);
        UsuarioMassa usuarioValido = massa.getUsuarioValido();
        LoginRequest request = new LoginRequest(usuarioValido.getEmail(), usuarioValido.getSenha());
        String jsonSchema = JsonSchemaGenerator.generate(AuthResponse.class);

        authController.login(request)
                .then()
                .statusCode(200)
                .body(matchesJsonSchema(jsonSchema));
    }

    @Test(description = "Contrato de POST /auth/registro deve respeitar o schema de AuthResponse")
    public void validarContratoDeRegistro() {
        RegistroRequest request = new RegistroRequest("Usuario Contrato", emailUnico(), "senha123");
        String jsonSchema = JsonSchemaGenerator.generate(AuthResponse.class);

        AuthResponse resposta = authController.registrar(request)
                .then()
                .statusCode(201)
                .body(matchesJsonSchema(jsonSchema))
                .extract()
                .as(AuthResponse.class);
        usuariosCriados.add(resposta.usuario().id());
    }

    private static String emailUnico() {
        return "contrato." + UUID.randomUUID() + "@teste.com";
    }
}
