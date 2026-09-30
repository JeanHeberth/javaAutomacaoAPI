package com.br.api.requests;

import com.br.api.dto.auth.LoginRequest;
import com.br.api.dto.auth.RegistroRequest;
import io.restassured.response.Response;

/**
 * Chamadas HTTP dos endpoints de autenticacao (/v1/auth/**).
 */
public class AuthControllerRequests extends BaseController {

    public Response login(LoginRequest request) {
        return post("/auth/login", request);
    }

    public Response registrar(RegistroRequest request) {
        return post("/auth/registro", request);
    }
}
