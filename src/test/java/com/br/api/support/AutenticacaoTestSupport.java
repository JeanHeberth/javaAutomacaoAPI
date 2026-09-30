package com.br.api.support;

import com.br.api.client.AuthClient;
import com.br.api.config.ConfigLoader;
import com.br.api.massa.MassaArquivos;
import com.br.api.model.massa.MassaUsuarios;
import com.br.api.model.massa.UsuarioMassa;
import com.br.api.model.request.LoginRequest;
import com.br.api.model.request.RegistroRequest;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Suporte de autenticacao reutilizavel pelos testes: garante o usuario valido
 * da massa cadastrado e devolve um token JWT pronto para uso.
 */
public final class AutenticacaoTestSupport {

    private static final Logger log = LoggerFactory.getLogger(AutenticacaoTestSupport.class);

    private AutenticacaoTestSupport() {
    }

    public static String obterTokenUsuarioValido() {
        UsuarioMassa usuarioValido = carregarMassa().getUsuarioValido();
        garantirUsuarioRegistrado(usuarioValido);
        return login(usuarioValido);
    }

    public static MassaUsuarios carregarMassa() {
        return ConfigLoader.carregar(MassaArquivos.USUARIOS.getPath(), MassaUsuarios.class);
    }

    public static void garantirUsuarioRegistrado(UsuarioMassa usuario) {
        log.info("Garantindo usuario de teste registrado: email={}", usuario.getEmail());

        RegistroRequest registro = new RegistroRequest(usuario.getNome(), usuario.getEmail(), usuario.getSenha());

        Response resposta = AuthClient.registrar(registro);
        int status = resposta.statusCode();

        // 201: usuario criado agora | 409: usuario ja existia de uma execucao anterior
        if (status != 201 && status != 409) {
            log.error("Falha ao preparar usuario de teste. HTTP {} - {}", status, resposta.asString());
            throw new IllegalStateException(
                    "Falha ao preparar usuario de teste. HTTP " + status + " - " + resposta.asString());
        }

        log.debug("Usuario de teste pronto (HTTP {}): email={}", status, usuario.getEmail());
    }

    public static String login(UsuarioMassa usuario) {
        log.info("Autenticando usuario: email={}", usuario.getEmail());

        LoginRequest login = new LoginRequest(usuario.getEmail(), usuario.getSenha());
        String token = AuthClient.login(login)
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        log.debug("Token obtido com sucesso para email={}", usuario.getEmail());
        return token;
    }
}
