package com.br.api.utils;

import com.br.api.requests.ManutencaoControllerRequests;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Classe base para as classes de teste de API: centraliza a configuracao
 * comum do RestAssured (log de request/response quando uma asserção falha),
 * o acesso ao token de autenticacao da massa de teste, e a limpeza do banco
 * antes da suite.
 */
public abstract class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    private static final ManutencaoControllerRequests manutencaoController = new ManutencaoControllerRequests();

    // static: compartilhado por TODAS as subclasses (uma unica limpeza por
    // JVM/execucao, nao uma por classe de teste).
    private static final AtomicBoolean bancoLimpo = new AtomicBoolean(false);

    @BeforeClass
    public void configurarRestAssured() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
    }

    @BeforeClass(dependsOnMethods = "configurarRestAssured")
    public void limparBancoUmaVezPorSuite() {
        if (bancoLimpo.compareAndSet(false, true)) {
            String token = obterToken();
            int status = manutencaoController.limparBanco(token).statusCode();
            log.info("Banco de dados limpo antes da suite (uma unica vez, HTTP {})", status);
        }
    }

    protected static String obterToken() {
        return TestSupport.obterTokenUsuarioValido();
    }
}
