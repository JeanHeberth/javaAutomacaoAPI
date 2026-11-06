package com.br.api.dto.auth;

import com.br.api.dto.api.UsuarioResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthResponse(String token, String tipo, UsuarioResponse usuario) {
}
