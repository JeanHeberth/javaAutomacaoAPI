package com.br.api.support;

import com.br.api.config.ConfigLoader;
import com.br.api.massa.MassaArquivos;
import com.br.api.model.massa.ProdutoMassa;
import com.br.api.model.request.ProdutoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Suporte de massa de produto reutilizavel pelos testes: o "nome" da massa e
 * apenas um prefixo legivel, sempre combinado com um sufixo unico (UUID) para
 * respeitar a restricao de unicidade da API e manter os testes repetiveis
 * entre execucoes.
 */
public final class ProdutoTestSupport {

    private static final Logger log = LoggerFactory.getLogger(ProdutoTestSupport.class);

    private ProdutoTestSupport() {
    }

    public static ProdutoMassa carregarMassa() {
        return ConfigLoader.carregar(MassaArquivos.PRODUTOS.getPath(), ProdutoMassa.class);
    }

    public static ProdutoRequest produtoValido() {
        ProdutoMassa massa = carregarMassa();
        ProdutoRequest request = new ProdutoRequest(
                nomeUnico(massa.getNome()),
                massa.getDescricao(),
                massa.getPreco(),
                massa.getEstoque(),
                massa.getCategoria());

        log.info("Produto de teste gerado: nome={}", request.nome());
        return request;
    }

    public static String nomeUnico(String prefixo) {
        return prefixo + " " + UUID.randomUUID();
    }
}
