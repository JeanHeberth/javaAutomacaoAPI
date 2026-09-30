package com.br.api.requests;

import io.restassured.response.Response;

import java.util.Map;

import static com.br.api.util.Endpoints.USUARIOS;

public class UsuarioControllerRequests extends BaseController {

    public Response listarUsuarios(String token, Map<String, ?> queryParams) {
        return get(USUARIOS.getPath(), token, queryParams);
    }

    public Response listarUsuariosSemToken(Map<String, ?> queryParams) {
        return get(USUARIOS.getPath(), queryParams);
    }

    public Response excluirUsuario(String token, Long id) {
        return delete(USUARIOS.getPath() + "/" + id, token);
    }
}
