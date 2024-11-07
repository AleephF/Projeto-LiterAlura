package br.com.alura.projetoliteralura.repositorios;

import br.com.alura.projetoliteralura.modelos.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepositorio extends JpaRepository<Autor, Long> {


    Autor findByNomeAndAnoDeNascimentoAndAnoDeFalecimento(String nome, Integer anoDeNascimento, Integer anoDeFalecimento);
}
