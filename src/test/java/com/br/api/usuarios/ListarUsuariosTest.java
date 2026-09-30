package com.br.api.usuarios;

import com.br.api.client.UsuarioClient;
import com.br.api.model.massa.UsuarioMassa;
import com.br.api.support.AutenticacaoTestSupport;
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
        // "obterToken()" garante que o usuario valido da massa (usuarios.yaml)
        // esta cadastrado — e o unico usuario cuja existencia o teste pode
        // assumir, ja que o banco e limpo antes da suite (ver BaseTest).
        UsuarioMassa usuarioValido = AutenticacaoTestSupport.carregarMassa().getUsuarioValido();

        List<String> nomesUsuarios = UsuarioClient.listarUsuarios(token, Collections.emptyMap())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("nome", String.class);

        assertThat(nomesUsuarios).contains(usuarioValido.getNome());
    }
}