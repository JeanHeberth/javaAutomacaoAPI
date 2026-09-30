package com.br.api.auth;

import com.br.api.client.AuthClient;
import com.br.api.client.UsuarioClient;
import com.br.api.model.request.RegistroRequest;
import com.br.api.model.response.AuthResponse;
import com.br.api.support.BaseTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de POST /v1/auth/registro (registro no swagger).
 *
 * O email de cada usuario e gerado dinamicamente (UUID) para que os testes
 * continuem repetiveis mesmo sem reset de banco entre execucoes. Todo
 * usuario criado com sucesso e registrado em {@code usuariosCriados} e
 * removido no @AfterAll, para nao deixar sujeira no banco.
 */
class RegistrarUsuarioTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(RegistrarUsuarioTest.class);
    private static final List<Long> usuariosCriados = new ArrayList<>();

    private static String token;

    @BeforeAll
    static void autenticar() {
        token = obterToken();
    }

    @AfterAll
    static void limparUsuariosCriados() {
        usuariosCriados.forEach(id -> {
            int status = UsuarioClient.excluirUsuario(token, id).statusCode();
            log.info("Limpeza: usuario id={} removido (HTTP {})", id, status);
        });
    }

    @Test
    @DisplayName("Registrar usuario com dados validos deve retornar 201 e um token JWT")
    void registrarUsuarioComDadosValidosDeveRetornar201() {
        RegistroRequest request = usuarioNovoValido();

        AuthResponse resposta = AuthClient.registrar(request)
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

    @ParameterizedTest(name = "{0}")
    @MethodSource("payloadsInvalidos")
    @DisplayName("Registrar usuario com dados invalidos deve retornar 400")
    void registrarUsuarioComDadosInvalidosDeveRetornar400(String cenario, RegistroRequest request) {
        AuthClient.registrar(request)
                .then()
                .statusCode(400);
    }

    static Stream<Arguments> payloadsInvalidos() {
        return Stream.of(
                Arguments.of("Nome muito curto", new RegistroRequest("Jo", emailUnico(), "senha123")),
                Arguments.of("Nome em branco", new RegistroRequest("", emailUnico(), "senha123")),
                Arguments.of("Email invalido", new RegistroRequest("Usuario Teste", "email-invalido", "senha123")),
                Arguments.of("Senha muito curta", new RegistroRequest("Usuario Teste", emailUnico(), "123"))
        );
    }

    @Test
    @DisplayName("Registrar usuario com email ja cadastrado deve retornar 409")
    void registrarUsuarioComEmailDuplicadoDeveRetornar409() {
        RegistroRequest request = usuarioNovoValido();

        Long idCriado = AuthClient.registrar(request)
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("usuario.id");
        usuariosCriados.add(idCriado);

        AuthClient.registrar(request)
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
