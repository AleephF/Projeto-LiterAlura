package br.com.alura.projetoliteralura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DadosAutor.Autores> autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") int numeroDownloads) {}
