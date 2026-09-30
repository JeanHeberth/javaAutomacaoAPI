package com.br.api.support;

import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.BeforeAll;

/**
 * Classe base para as classes de teste de API: centraliza a configuracao
 * comum do RestAssured (log de request/response quando uma asserção falha)
 * e o acesso ao token de autenticacao da massa de teste.
 */
public abstract class BaseTest {

    @BeforeAll
    static void configurarRestAssured() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
    }

    protected static String obterToken() {
        return AutenticacaoTestSupport.obterTokenUsuarioValido();
    }
}
