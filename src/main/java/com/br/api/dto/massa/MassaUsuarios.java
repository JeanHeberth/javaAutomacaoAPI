package com.br.api.dto.massa;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MassaUsuarios {

    private UsuarioMassa usuarioValido;
    private List<CasoLoginInvalido> casosLoginInvalido;
}
