package br.com.alura.projetoliteralura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAutor(@JsonAlias("authors")List<Autores> autores){

    public record Autores(@JsonAlias("name") String nome,
                          @JsonAlias("birth_year") Integer anoDeNascimento,
                          @JsonAlias("death_year") Integer anoDeFalecimento) {}
}
