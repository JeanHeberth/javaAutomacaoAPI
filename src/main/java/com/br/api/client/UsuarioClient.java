package com.br.api.client;

import com.br.api.endpoint.Endpoints;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;

import java.util.Map;

import static com.br.api.endpoint.Endpoints.*;

@AllArgsConstructor
public class UsuarioClient {

    public static Response listarUsuarios(String token, Map<String, ?> queryParams) {
        return ApiClients.given()
                .header("Authorization", "Bearer " + token)
                .queryParams(queryParams)
                .when()
                .get(USUARIOS.getPath());
    }

    public static Response listarUsuariosSemToken(Map<String, ?> queryParams) {
        return ApiClients.given()
                .queryParams(queryParams)
                .when()
                .get(USUARIOS.getPath());
    }
}
