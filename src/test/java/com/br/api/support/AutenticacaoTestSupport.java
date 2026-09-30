package com.br.api.support;

import com.br.api.client.AuthClient;
import com.br.api.config.ConfigLoader;
import com.br.api.model.massa.MassaUsuarios;
import com.br.api.model.massa.UsuarioMassa;
import com.br.api.model.request.LoginRequest;
import com.br.api.model.request.RegistroRequest;
import io.restassured.response.Response;

/**
 * Suporte de autenticacao reutilizavel pelos testes: garante o usuario valido
 * da massa cadastrado e devolve um token JWT pronto para uso.
 */
public final class AutenticacaoTestSupport {

    private static final String MASSA_USUARIOS = "massa/usuarios.yaml";

    private AutenticacaoTestSupport() {
    }

    public static String obterTokenUsuarioValido() {
        UsuarioMassa usuarioValido = carregarMassa().getUsuarioValido();
        garantirUsuarioRegistrado(usuarioValido);
        return login(usuarioValido);
    }

    public static MassaUsuarios carregarMassa() {
        return ConfigLoader.carregar(MASSA_USUARIOS, MassaUsuarios.class);
    }

    public static void garantirUsuarioRegistrado(UsuarioMassa usuario) {
        RegistroRequest registro = new RegistroRequest(usuario.getNome(), usuario.getEmail(), usuario.getSenha());

        Response resposta = AuthClient.registrar(registro);
        int status = resposta.statusCode();

        // 201: usuario criado agora | 409: usuario ja existia de uma execucao anterior
        if (status != 201 && status != 409) {
            throw new IllegalStateException(
                    "Falha ao preparar usuario de teste. HTTP " + status + " - " + resposta.asString());
        }
    }

    public static String login(UsuarioMassa usuario) {
        LoginRequest login = new LoginRequest(usuario.getEmail(), usuario.getSenha());
        return AuthClient.login(login)
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }
}
