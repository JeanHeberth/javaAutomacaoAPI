package com.br.api.model.massa;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MassaUsuarios {

    private UsuarioMassa usuarioValido;
    private List<CasoLoginInvalido> casosLoginInvalido;
}
