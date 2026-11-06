package com.br.api.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;

/**
 * Gera o JSON Schema de um DTO em tempo de execucao (via reflection), para
 * validar o contrato das respostas da API com RestAssured
 * (matchesJsonSchema). Evita manter arquivos .json de schema versionados
 * manualmente e dessincronizados dos DTOs reais.
 *
 * Uso: generate(ProdutoResponse.class) para um objeto unico, ou
 * generate(List.class, ProdutoResponse.class) para uma lista do tipo.
 */
public final class JsonSchemaGenerator {

    private static final SchemaGenerator GENERATOR = build();

    private JsonSchemaGenerator() {
    }

    public static String generate(Class<?> mainTargetType, Class<?>... typeParameters) {
        JsonNode schema = GENERATOR.generateSchema(mainTargetType, typeParameters);
        return schema.toString();
    }

    private static SchemaGenerator build() {
        SchemaGeneratorConfigBuilder configBuilder =
                new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
        SchemaGeneratorConfig config = configBuilder.build();
        return new SchemaGenerator(config);
    }
}
