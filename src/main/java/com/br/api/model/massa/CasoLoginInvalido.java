package com.br.api.model.massa;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CasoLoginInvalido {

    private String descricao;
    private String email;
    private String senha;
    private int statusEsperado;

    @Override
    public String toString() {
        return descricao;
    }
}
