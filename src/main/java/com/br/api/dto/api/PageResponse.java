package com.br.api.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Pagina generica do Spring Data, formato comum as listagens da API
 * (/produtos, /pedidos). O Spring Data retorna campos adicionais (pageable,
 * sort, etc.) que nao fazem parte do schema documentado no swagger; eles sao
 * ignorados aqui para nao acoplar o teste a detalhes de implementacao.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PageResponse<T>(
        Integer totalPages,
        Long totalElements,
        Boolean first,
        Boolean last,
        Integer numberOfElements,
        Integer size,
        Integer number,
        Boolean empty,
        List<T> content) {
}
