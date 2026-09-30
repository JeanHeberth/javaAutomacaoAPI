package com.br.api.client;

import com.br.api.model.request.LoginRequest;
import com.br.api.model.request.RegistroRequest;
import io.restassured.response.Response;

/**
 * Chamadas HTTP dos endpoints de autenticacao (/v1/auth/**).
 */
public final class AuthClient {

    private AuthClient() {
    }

    public static Response login(LoginRequest request) {
        return ApiClients.given()
                .body(request)
                .when()
                .post("/auth/login");
    }

    public static Response registrar(RegistroRequest request) {
        return ApiClients.given()
                .body(request)
                .when()
                .post("/auth/registro");
    }
}
