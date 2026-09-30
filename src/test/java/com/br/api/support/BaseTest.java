package com.br.api.support;

import com.br.api.client.ManutencaoClient;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Classe base para as classes de teste de API: centraliza a configuracao
 * comum do RestAssured (log de request/response quando uma asserção falha),
 * o acesso ao token de autenticacao da massa de teste, e a limpeza do banco
 * antes da suite.
 */
public abstract class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    // static na superclasse: compartilhado por TODAS as subclasses (uma unica
    // limpeza por JVM/execucao, nao uma por classe de teste).
    private static final AtomicBoolean bancoLimpo = new AtomicBoolean(false);

    @BeforeAll
    static void configurarRestAssured() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
    }

    @BeforeAll
    static void limparBancoUmaVezPorSuite() {
        if (bancoLimpo.compareAndSet(false, true)) {
            String token = obterToken();
            int status = ManutencaoClient.limparBanco(token).statusCode();
            log.info("Banco de dados limpo antes da suite (uma unica vez, HTTP {})", status);
        }
    }

    protected static String obterToken() {
        return AutenticacaoTestSupport.obterTokenUsuarioValido();
    }
}
