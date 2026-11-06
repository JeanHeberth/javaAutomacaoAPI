package com.br.api.requests;

import io.restassured.response.Response;

public class ActuatorControllerRequests extends BaseController {

    private static final String ACTUATOR_HEALTH = "/actuator/health";

    public Response getHealth() {
        return get(ACTUATOR_HEALTH);
    }
}
