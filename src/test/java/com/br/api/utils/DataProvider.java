package com.br.api.utils;

import com.br.api.dto.massa.ProdutoMassa;
import com.br.api.util.ConfigLoader;
import com.br.api.util.MassaArquivos;

/**
 * DataProviders (TestNG) reutilizaveis pelos testes parametrizados
 * (@Test(dataProvider = "...", dataProviderClass = DataProvider.class)),
 * centralizados aqui para nao duplicar massa de casos entre classes de
 * teste.
 */
public final class DataProvider {

    private DataProvider() {
    }

    /**
     * Ids invalidos para os endpoints de produto que recebem {id} na URL
     * (GET e DELETE /produtos/{id}): cobre tanto ids com formato valido de
     * Long mas inexistentes/fora do dominio (404), quanto ids com formato
     * invalido para o path variable (400).
     */
    @org.testng.annotations.DataProvider(name = "idsInvalidosProvider")
    public static Object[][] idsInvalidosProvider() {
        return new Object[][]{
                {"Id inexistente", "999999999", 404},
                {"Id negativo", "-1", 404},
                {"Id zero", "0", 404},
                {"Id nao numerico", "abc", 400},
                {"Id decimal", "1.5", 400},
                {"Id maior que Long.MAX_VALUE", "99999999999999999999", 400}
        };
    }

    /**
     * Nome do produto valido cadastrado em massa/produtos.yaml, usado para
     * validar que o nome devolvido pela API corresponde ao informado na
     * massa (o nome efetivamente enviado na requisicao leva um sufixo unico
     * via ProdutoTestSupport.nomeUnico, entao a comparacao e feita com
     * startsWith).
     */
    @org.testng.annotations.DataProvider(name = "nomeProdutoMassaProvider")
    public static Object[][] nomeProdutoMassaProvider() {
        ProdutoMassa massa = ConfigLoader.carregar(MassaArquivos.PRODUTOS.getPath(), ProdutoMassa.class);
        return new Object[][]{
                {massa.getNome()}
        };
    }
}
