package com.br.api.dto.api;

import java.math.BigDecimal;

public record ProdutoRequest(
        String nome,
        String descricao,
        BigDecimal preco,
        Integer estoque,
        String categoria) {
}
