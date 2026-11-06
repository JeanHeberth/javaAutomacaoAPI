package com.br.api.auth;

import com.br.api.dto.api.ErroResponse;
import com.br.api.dto.auth.AuthResponse;
import com.br.api.dto.auth.LoginRequest;
import com.br.api.dto.massa.CasoLoginInvalido;
import com.br.api.dto.massa.MassaUsuarios;
import com.br.api.dto.massa.UsuarioMassa;
import com.br.api.requests.AuthControllerRequests;
import com.br.api.util.ConfigLoader;
import com.br.api.util.MassaArquivos;
import com.br.api.utils.TestSupport;
import com.br.api.utils.BaseTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Testes de POST /v1/auth/login.
 */
public class LoginTest extends BaseTest {

    private final AuthControllerRequests authController = new AuthControllerRequests();

    private static MassaUsuarios massa;

    @BeforeClass
    public void prepararUsuarioDeTeste() {
        massa = ConfigLoader.carregar(MassaArquivos.USUARIOS.getPath(), MassaUsuarios.class);
        TestSupport.garantirUsuarioRegistrado(massa.getUsuarioValido());
    }

    @Test(description = "Login com credenciais validas deve retornar 200 e um token JWT")
    public void loginComCredenciaisValidasDeveRetornarToken() {
        UsuarioMassa usuario = massa.getUsuarioValido();
        LoginRequest request = new LoginRequest(usuario.getEmail(), usuario.getSenha());

        AuthResponse resposta = authController.login(request)
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

    @Test(dataProvider = "casosLoginInvalido", description = "Login com credenciais invalidas deve retornar o status de erro esperado")
    public void loginComCredenciaisInvalidasDeveFalhar(CasoLoginInvalido caso) {
        LoginRequest request = new LoginRequest(caso.getEmail(), caso.getSenha());

        ErroResponse erro = authController.login(request)
                .then()
                .statusCode(caso.getStatusEsperado())
                .extract()
                .as(ErroResponse.class);

        assertThat(erro.status()).isEqualTo(caso.getStatusEsperado());
        assertThat(erro.mensagem()).isNotBlank();
    }

    @DataProvider(name = "casosLoginInvalido")
    public Object[][] casosLoginInvalido() {
        MassaUsuarios massaCasos = ConfigLoader.carregar(MassaArquivos.USUARIOS.getPath(), MassaUsuarios.class);
        return massaCasos.getCasosLoginInvalido().stream()
                .map(caso -> new Object[]{caso})
                .toArray(Object[][]::new);
    }
}
