package com.br.api.tests.health;

import com.br.api.client.HealthClient;
import com.br.api.model.response.HealthResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HealthCheckTest {

    @Test
    @DisplayName("Verificar saúde da aplicação")
    void verificarSaudeDaAplicacao() {
        HealthResponse healthResponse = HealthClient.verificarSaude()
                .then()
                .statusCode(200)
                .extract()
                .as(HealthResponse.class);

        assertThat(healthResponse.getStatus()).isEqualTo("UP");
    }
}
