package com.br.api.health;

import com.br.api.client.HealthClient;
import com.br.api.model.response.HealthResponse;
import com.br.api.support.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HealthCheckTest extends BaseTest {

    @Test
    @DisplayName("Verificar saúde da aplicação")
    void verificarSaudeDaAplicacao() {
        HealthResponse healthResponse = HealthClient.verificarSaude()
                .then()
                .statusCode(200)
                .extract()
                .as(HealthResponse.class);

        assertThat(healthResponse.status()).isEqualTo("UP");
    }
}
