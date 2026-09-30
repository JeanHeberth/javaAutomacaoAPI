package com.br.api.client;

import com.br.api.config.ConfigAmbiente;
import com.br.api.config.ConfigLoader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/**
 * Especificacao base do RestAssured, montada a partir de config/ambiente.yaml
 * (lido via SnakeYAML).
 */
public final class ApiClients {

    private static final ConfigAmbiente CONFIG = ConfigLoader.carregar("config/ambiente.yaml", ConfigAmbiente.class);

    private ApiClients() {
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
     * em cada client. O filtro do Allure anexa request e response de cada
     * chamada ao relatorio.
     */
    public static RequestSpecification given() {
        return RestAssured.given()
                .spec(specPadrao())
                .filter(new AllureRestAssured());
    }
}
