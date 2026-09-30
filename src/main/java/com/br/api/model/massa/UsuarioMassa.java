package com.br.api.model.massa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioMassa {

    private String nome;
    private String email;
    private String senha;
}
