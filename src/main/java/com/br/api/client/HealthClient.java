package com.br.api.client;

import com.br.api.endpoint.Endpoints;
import io.restassured.response.Response;

import static com.br.api.endpoint.Endpoints.*;

public class HealthClient {

    private static final String HEALTH_ENDPOINT = HEALTH.getPath();

    private HealthClient() {
    }

    public static Response verificarSaude(){
        return ApiClients.given()
                .when()
                .get(HEALTH_ENDPOINT);
    }
}
