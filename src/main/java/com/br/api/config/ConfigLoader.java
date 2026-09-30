package com.br.api.config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;

/**
 * Le arquivos .yaml do classpath (src/test/resources) e mapeia para POJOs
 * usando SnakeYAML.
 */
public final class ConfigLoader {

    private ConfigLoader() {
    }

    public static <T> T carregar(String caminhoRecurso, Class<T> tipo) {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(caminhoRecurso)) {
            if (input == null) {
                throw new IllegalArgumentException("Arquivo YAML nao encontrado no classpath: " + caminhoRecurso);
            }
            Yaml yaml = new Yaml(new Constructor(tipo, new LoaderOptions()));
            return yaml.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao carregar arquivo YAML: " + caminhoRecurso, e);
        }
    }
}
