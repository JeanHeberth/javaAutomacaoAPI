package com.br.api.auth;

import com.br.api.dto.auth.AuthResponse;
import com.br.api.dto.auth.RegistroRequest;
import com.br.api.requests.AuthControllerRequests;
import com.br.api.requests.UsuarioControllerRequests;
import com.br.api.utils.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de POST /v1/auth/registro (registro no swagger).
 *
 * O email de cada usuario e gerado dinamicamente (UUID) para que os testes
 * continuem repetiveis mesmo sem reset de banco entre execucoes. Todo
 * usuario criado com sucesso e registrado em {@code usuariosCriados} e
 * removido no @AfterClass, para nao deixar sujeira no banco.
 */
public class RegistrarUsuarioTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(RegistrarUsuarioTest.class);
    private static final List<Long> usuariosCriados = new ArrayList<>();

    private final AuthControllerRequests authController = new AuthControllerRequests();
    private final UsuarioControllerRequests usuarioController = new UsuarioControllerRequests();

    private static String token;

    @BeforeClass
    public void autenticar() {
        token = obterToken();
    }

    @AfterClass
    public void limparUsuariosCriados() {
        usuariosCriados.forEach(id -> {
            int status = usuarioController.excluirUsuario(token, id).statusCode();
            log.info("Limpeza: usuario id={} removido (HTTP {})", id, status);
        });
    }

    @Test(description = "Registrar usuario com dados validos deve retornar 201 e um token JWT")
    public void registrarUsuarioComDadosValidosDeveRetornar201() {
        RegistroRequest request = usuarioNovoValido();

        AuthResponse resposta = authController.registrar(request)
                .then()
                .statusCode(201)
                .extract()
                .as(AuthResponse.class);
        usuariosCriados.add(resposta.usuario().id());

        assertThat(resposta.token()).isNotBlank();
        assertThat(resposta.tipo()).isEqualTo("Bearer");
        assertThat(resposta.usuario()).isNotNull();
        assertThat(resposta.usuario().nome()).isEqualTo(request.nome());
        assertThat(resposta.usuario().email()).isEqualTo(request.email());
    }

    @Test(dataProvider = "payloadsInvalidos", description = "Registrar usuario com dados invalidos deve retornar 400")
    public void registrarUsuarioComDadosInvalidosDeveRetornar400(String cenario, RegistroRequest request) {
        authController.registrar(request)
                .then()
                .statusCode(400);
    }

    @DataProvider(name = "payloadsInvalidos")
    public Object[][] payloadsInvalidos() {
        return new Object[][]{
                {"Nome muito curto", new RegistroRequest("Jo", emailUnico(), "senha123")},
                {"Nome em branco", new RegistroRequest("", emailUnico(), "senha123")},
                {"Email invalido", new RegistroRequest("Usuario Teste", "email-invalido", "senha123")},
                {"Senha muito curta", new RegistroRequest("Usuario Teste", emailUnico(), "123")}
        };
    }

    @Test(description = "Registrar usuario com email ja cadastrado deve retornar 409")
    public void registrarUsuarioComEmailDuplicadoDeveRetornar409() {
        RegistroRequest request = usuarioNovoValido();

        Long idCriado = authController.registrar(request)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("usuario.id");
        usuariosCriados.add(idCriado);

        authController.registrar(request)
                .then()
                .statusCode(409);
    }

    private static RegistroRequest usuarioNovoValido() {
        return new RegistroRequest("Usuario Teste Automatizado", emailUnico(), "senha123");
    }

    private static String emailUnico() {
        return "usuario.teste." + UUID.randomUUID() + "@teste.com";
    }
}
