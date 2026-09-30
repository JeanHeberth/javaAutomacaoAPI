package com.br.api.auth;

import com.br.api.client.AuthClient;
import com.br.api.config.ConfigLoader;
import com.br.api.model.massa.CasoLoginInvalido;
import com.br.api.model.massa.MassaUsuarios;
import com.br.api.model.massa.UsuarioMassa;
import com.br.api.model.request.LoginRequest;
import com.br.api.model.response.AuthResponse;
import com.br.api.model.response.ErroResponse;
import com.br.api.support.AutenticacaoTestSupport;
import com.br.api.support.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Testes de POST /v1/auth/login.
 */
class LoginTest extends BaseTest {

    private static final String MASSA_USUARIOS = "massa/usuarios.yaml";

    private static MassaUsuarios massa;

    @BeforeAll
    static void prepararUsuarioDeTeste() {
        massa = ConfigLoader.carregar(MASSA_USUARIOS, MassaUsuarios.class);
        AutenticacaoTestSupport.garantirUsuarioRegistrado(massa.getUsuarioValido());
    }

    @Test
    @DisplayName("Login com credenciais validas deve retornar 200 e um token JWT")
    void loginComCredenciaisValidasDeveRetornarToken() {
        UsuarioMassa usuario = massa.getUsuarioValido();
        LoginRequest request = new LoginRequest(usuario.getEmail(), usuario.getSenha());

        AuthResponse resposta = AuthClient.login(request)
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("tipo", equalTo("Bearer"))
                .body("usuario.email", equalTo(usuario.getEmail()))
                .extract()
                .as(AuthResponse.class);

        assertThat(resposta.token()).isNotBlank();
        assertThat(resposta.tipo()).isEqualTo("Bearer");
        assertThat(resposta.usuario()).isNotNull();
        assertThat(resposta.usuario().email()).isEqualTo(usuario.getEmail());
        assertThat(resposta.usuario().nome()).isEqualTo(usuario.getNome());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("casosLoginInvalido")
    @DisplayName("Login com credenciais invalidas deve retornar o status de erro esperado")
    void loginComCredenciaisInvalidasDeveFalhar(CasoLoginInvalido caso) {
        LoginRequest request = new LoginRequest(caso.getEmail(), caso.getSenha());

        ErroResponse erro = AuthClient.login(request)
                .then()
                .statusCode(caso.getStatusEsperado())
                .extract()
                .as(ErroResponse.class);

        assertThat(erro.status()).isEqualTo(caso.getStatusEsperado());
        assertThat(erro.mensagem()).isNotBlank();
    }

    static Stream<CasoLoginInvalido> casosLoginInvalido() {
        MassaUsuarios massaCasos = ConfigLoader.carregar(MASSA_USUARIOS, MassaUsuarios.class);
        return massaCasos.getCasosLoginInvalido().stream();
    }
}
