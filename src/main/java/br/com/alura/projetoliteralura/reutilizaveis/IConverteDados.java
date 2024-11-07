package br.com.alura.projetoliteralura.reutilizaveis;

public interface IConverteDados {
    <T> T  obterDados(String json, Class<T> classe);
}
