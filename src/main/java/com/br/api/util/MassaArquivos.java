package com.br.api.util;

import lombok.Getter;

@Getter
public enum MassaArquivos {

    USUARIOS("massa/usuarios.yaml"),
    PRODUTOS("massa/produtos.yaml");

    private final String path;

    MassaArquivos(String path) {
        this.path = path;
    }

}
