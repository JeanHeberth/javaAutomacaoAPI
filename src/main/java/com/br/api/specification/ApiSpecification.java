package com.br.api.specification;

import com.br.api.util.ConfigAmbiente;
import com.br.api.util.ConfigLoader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Especificacao base do RestAssured, montada a partir de dev-properties.yaml
 * (lido via SnakeYAML).
 */
public final class ApiSpecification {

    private static final ConfigAmbiente CONFIG = ConfigLoader.carregar("dev-properties.yaml", ConfigAmbiente.class);

    private ApiSpecification() {
    }

    public static RequestSpecification specPadrao() {
        return new RequestSpecBuilder()
                .setBaseUri(CONFIG.getBaseUri())
                .setBasePath(CONFIG.getBasePath())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    /**
     * Ponto de entrada padrao das requisicoes: equivalente a
     * {@code RestAssured.given().spec(specPadrao())}, evitando repetir o spec
     * em cada classe de requests. O filtro do Allure anexa request e response
     * de cada chamada ao relatorio.
     */
    public static RequestSpecification given() {
        return RestAssured.given()
                .spec(specPadrao())
                .filter(new AllureRestAssured());
    }
}
