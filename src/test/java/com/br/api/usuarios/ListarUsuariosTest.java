package com.br.api.usuarios;

import com.br.api.client.ProdutoClient;
import com.br.api.client.UsuarioClient;
import com.br.api.support.BaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ListarUsuariosTest extends BaseTest {

    private static String token;

    @BeforeAll
    static void setUp() {
        token = obterToken();
    }

    @Test
    @DisplayName("Nao Deve listar usuários sem token de autenticação")
    void listarUsuariosSemToken() {
        UsuarioClient.listarUsuariosSemToken(Collections.emptyMap())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Deve listar usuários com token de autenticação válido")
    void listarUsuariosComTokenValido() {

        List<String> nomesUsuarios = UsuarioClient.listarUsuarios(token, Collections.emptyMap())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("nome", String.class);

        assertThat(nomesUsuarios).contains("João da Silva");
    }
}