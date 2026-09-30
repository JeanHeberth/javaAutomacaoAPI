package com.br.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * O Spring Data retorna campos adicionais (pageable, sort, etc.) que nao
 * fazem parte do schema PagePedidoResponse documentado no swagger; eles sao
 * ignorados aqui para nao acoplar o teste a detalhes de implementacao.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PagePedidoResponse(
        Integer totalPages,
        Long totalElements,
        Boolean first,
        Boolean last,
        Integer numberOfElements,
        Integer size,
        Integer number,
        Boolean empty,
        List<PedidoResponse> content) {
}
