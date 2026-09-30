package com.br.api.utils;

import io.qameta.allure.Allure;

/**
 * Anexa informacoes extras (massa usada, payloads, observacoes) aos
 * relatorios do Allure durante a execucao dos testes, sem acoplar as classes
 * de teste a detalhes de como o anexo e feito.
 */
public final class TestReporter {

    private TestReporter() {
    }

    public static void step(String mensagem) {
        Allure.step(mensagem);
    }

    public static void anexar(String nome, String conteudo) {
        Allure.addAttachment(nome, conteudo);
    }
}
