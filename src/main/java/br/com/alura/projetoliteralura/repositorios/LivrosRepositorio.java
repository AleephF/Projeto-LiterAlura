package br.com.alura.projetoliteralura.repositorios;

import br.com.alura.projetoliteralura.modelos.Autor;
import br.com.alura.projetoliteralura.modelos.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivrosRepositorio extends JpaRepository<Livro, Long> {
    List<Livro> findByTituloContainingIgnoreCase(Livro livroLocalizados);
    boolean existsByTitulo(String titulo);


    @Query("SELECT a FROM Autor a WHERE a.anoDeNascimento <= :autorPorEpoca AND a.anoDeFalecimento >= :autorPorEpoca")
    List<Autor> autoresVivoPorEpoca(String autorPorEpoca);

    List<Livro> findByIdiomaContainingIgnoreCase(String livroPorIdioma);
}
