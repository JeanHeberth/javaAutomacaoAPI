package com.br.api.healthcheck;

import com.br.api.dto.api.HealthResponse;
import com.br.api.requests.ActuatorControllerRequests;
import com.br.api.utils.BaseTest;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HealthCheckTest extends BaseTest {

    private final ActuatorControllerRequests actuatorController = new ActuatorControllerRequests();

    @Test(description = "Verificar saude da aplicacao")
    public void validarSaudeDaAplicacao() {
        HealthResponse healthResponse = actuatorController.getHealth()
                .then()
                .statusCode(200)
                .extract()
                .as(HealthResponse.class);

        assertThat(healthResponse.status()).isEqualTo("UP");
    }
}
