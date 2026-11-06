package com.br.api.requests;

import com.br.api.specification.ApiSpecification;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Collections;
import java.util.Map;

/**
 * Base para as classes de requests: concentra o acesso ao RestAssured
 * (via ApiSpecification) e os verbos HTTP genericos usados pelas subclasses
 * especificas de cada controller da API.
 */
public abstract class BaseController {

    protected Response get(String path) {
        return get(path, null, Collections.emptyMap());
    }

    protected Response get(String path, Map<String, ?> queryParams) {
        return get(path, null, queryParams);
    }

    protected Response get(String path, String token, Map<String, ?> queryParams) {
        return autenticado(token)
                .queryParams(queryParams)
                .when()
                .get(path);
    }

    protected Response post(String path, Object body) {
        return post(path, null, body);
    }

    protected Response post(String path, String token, Object body) {
        return autenticado(token)
                .body(body)
                .when()
                .post(path);
    }

    protected Response delete(String path) {
        return delete(path, null);
    }

    protected Response delete(String path, String token) {
        return autenticado(token)
                .when()
                .delete(path);
    }

    private RequestSpecification autenticado(String token) {
        RequestSpecification spec = ApiSpecification.given();
        return token == null ? spec : spec.header("Authorization", "Bearer " + token);
    }
}
