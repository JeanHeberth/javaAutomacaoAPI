package com.br.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Formato de erro retornado pelo GlobalExceptionHandler da API
 * (ex.: 400, 401 de credenciais invalidas, 409). Erros de validacao (400)
 * trazem tambem um campo "campos", ignorado aqui pois nao e usado nas asserções.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ErroResponse(Integer status, String erro, String mensagem, String path, String timestamp) {
}
