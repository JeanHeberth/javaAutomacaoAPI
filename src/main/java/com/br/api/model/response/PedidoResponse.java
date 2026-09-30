package com.br.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PedidoResponse(
        Long id,
        Long usuarioId,
        String usuarioNome,
        List<ItemPedidoResponse> itens,
        String status,
        BigDecimal valorTotal,
        String criadoEm,
        String atualizadoEm) {
}
