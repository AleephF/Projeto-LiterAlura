package br.com.alura.projetoliteralura;

import br.com.alura.projetoliteralura.principal.Principal;
import br.com.alura.projetoliteralura.repositorios.AutorRepositorio;
import br.com.alura.projetoliteralura.repositorios.LivrosRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetoLiteraluraApplication implements CommandLineRunner {
	@Autowired
	private LivrosRepositorio repositorio;

	@Autowired
	private AutorRepositorio repositorioAutor;

	@Autowired
	private Principal principal;

	public static void main(String[] args) {
		SpringApplication.run(ProjetoLiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.exibePrincipal();
	}
}
