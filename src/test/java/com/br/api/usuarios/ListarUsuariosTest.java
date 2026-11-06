package com.br.api.usuarios;

import com.br.api.dto.massa.UsuarioMassa;
import com.br.api.requests.UsuarioControllerRequests;
import com.br.api.utils.TestSupport;
import com.br.api.utils.BaseTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListarUsuariosTest extends BaseTest {

    private final UsuarioControllerRequests usuarioController = new UsuarioControllerRequests();

    private static String token;

    @BeforeClass
    public void setUp() {
        token = obterToken();
    }

    @Test(description = "Nao deve listar usuarios sem token de autenticacao")
    public void listarUsuariosSemToken() {
        usuarioController.listarUsuariosSemToken(Collections.emptyMap())
                .then()
                .statusCode(401);
    }

    @Test(description = "Deve listar usuarios com token de autenticacao valido")
    public void listarUsuariosComTokenValido() {
        // "obterToken()" garante que o usuario valido da massa (usuarios.yaml)
        // esta cadastrado — e o unico usuario cuja existencia o teste pode
        // assumir, ja que o banco e limpo antes da suite (ver BaseTest).
        UsuarioMassa usuarioValido = TestSupport.carregarMassa().getUsuarioValido();

        List<String> nomesUsuarios = usuarioController.listarUsuarios(token, Collections.emptyMap())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("nome", String.class);

        assertThat(nomesUsuarios).contains(usuarioValido.getNome());
    }
}
