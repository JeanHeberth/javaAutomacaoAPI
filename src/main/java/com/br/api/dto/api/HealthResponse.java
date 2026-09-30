package com.br.api.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HealthResponse(String status, Map<String, Object> components) {
}
