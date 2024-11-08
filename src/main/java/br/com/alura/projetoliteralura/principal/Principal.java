package br.com.alura.projetoliteralura.principal;

import br.com.alura.projetoliteralura.modelos.*;
import br.com.alura.projetoliteralura.repositorios.AutorRepositorio;
import br.com.alura.projetoliteralura.repositorios.LivrosRepositorio;
import br.com.alura.projetoliteralura.reutilizaveis.ConsumoApi;
import br.com.alura.projetoliteralura.reutilizaveis.ConverteDados;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Principal {
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados converte = new ConverteDados();
    private LivrosRepositorio repositorio;
    private AutorRepositorio repositorioAutor;
    private List<Livro> livro = new ArrayList<>();
    private List<Autor> autor = new ArrayList<>();


    public Principal(LivrosRepositorio repositorio, AutorRepositorio repositorioAutor) {
        this.repositorio = repositorio;
        this.repositorioAutor = repositorioAutor;
    }

    public void exibePrincipal() {
        var opcoes = -1;

        while (opcoes != 0) {
            var menu = """
                    Escolha uma das opções:
                    1- Buscar livro pelo título
                    2- Listar livros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos em um determinado ano
                    5- Listar livros em um determinado idioma
                    
                    0- Sair
                    """;
            System.out.println(menu);
            opcoes = leitura.nextInt();
            leitura.nextLine();

            switch (opcoes) {
                case 1:
                    buscarLivroPeloTitulo();
                    break;
                case 2:
                    listarLivrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosPorEpoca();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção não encontrada!");
            }
        }
    }

    private void buscarLivroPeloTitulo() {

        List<DadosLivro> dadosLivros = getDadosLivro();
        for (DadosLivro dadosLivro : dadosLivros) {
            DadosAutor.Autores dadosAutor = dadosLivro.autores().get(0);
            Autor autor = new Autor(dadosAutor.nome(), dadosAutor.anoDeNascimento(), dadosAutor.anoDeFalecimento());
            Livro livroLocalizado = new Livro(dadosLivro, autor);

            Autor autorExistente = repositorioAutor.findByNomeAndAnoDeNascimentoAndAnoDeFalecimento(
                    autor.getNome(), autor.getAnoDeNascimento(), autor.getAnoDeFalecimento());

            if (autorExistente != null) {
                livroLocalizado.setAutor(autorExistente);
            } else {
                autorExistente = repositorioAutor.save(autor);
                livroLocalizado.setAutor(autorExistente);
            }

            if (repositorio.existsByTitulo(livroLocalizado.getTitulo())) {
                System.out.println("Livro já registrado!");
                return;
            }
            repositorio.save(livroLocalizado);
            System.out.println("**********************************************************");
            System.out.println("Titulo: " + livroLocalizado.getTitulo() + "\nAutor: " + livroLocalizado.getAutor().getNome() +
                    "\nIdioma: " + livroLocalizado.getIdioma() + "\nNúmero de downloads: " + livroLocalizado.getNumeroDownloads());
            System.out.println("**********************************************************");
            return;
        }
    }


    private List<DadosLivro> getDadosLivro() {
        System.out.println("Digite o nome do livro que deseja buscar: ");
        var livroBuscado = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + livroBuscado.toLowerCase().replace(" ", "+"));
        ResultadoLivros resultadoLivros = converte.obterDados(json, ResultadoLivros.class);
        return resultadoLivros.getResults();
    }

    private void listarLivrosRegistrados(){
        livro = repositorio.findAll();
        livro.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(l -> System.out.println("\n------ LIVRO ------" + "\nTitulo: " + l.getTitulo() + "\nAutor: " + l.getAutor().getNome() +
                        "\nIdioma: " + l.getIdioma() + "\nNúmero de downloads: " + l.getNumeroDownloads() + "\n-------------------" + "\n "));
    }

    private void listarAutoresRegistrados(){
        autor = repositorioAutor.findAll();
        livro = repositorio.findAll();
        autor.stream()
                .sorted(Comparator.comparing(Autor::getNome))
                .forEach(a -> System.out.println("*******************************" + "\nAutor: " + a.getNome() + "\nAno de nascimento: " + a.getAnoDeNascimento() + "\nAno de falecimento: " +
                        a.getAnoDeFalecimento() + "\nLivros: [" + livro.stream()
                                .filter(l -> l.getAutor().getNome().equals(a.getNome()))
                        .map(Livro::getTitulo)
                        .collect(Collectors.joining(", ")) + "]" + "\n*******************************\n"));
    }

    private void listarAutoresVivosPorEpoca(){
        System.out.println("Digite o ano que deseja pesquisar: ");
        var autorPorEpoca = leitura.nextLine();
        List<Autor> autorEncontrado = repositorio.autoresVivoPorEpoca(autorPorEpoca);
        livro = repositorio.findAll();
        autorEncontrado.stream()
                .filter(a -> a.getNome().equals(a.getNome()))
                .forEach(a -> System.out.println("\n*******************************" + "\nAutor: " + a.getNome() + "\nAno de nascimento: " + a.getAnoDeNascimento() + "\nAno de falecimento: "
                + a.getAnoDeFalecimento() + "\nLivro: [" + livro.stream()
                        .filter(l -> l.getAutor().getNome().equals(a.getNome()))
                        .map(Livro::getTitulo)
                        .collect(Collectors.joining(", ")) + "]" + "\n*******************************"));
    }

    private void listarLivrosPorIdioma(){
        var menuLivrosPorIdioma = """
                Digite o idioma do livro desejado:
                
                es - espanhol
                en - inglês
                fr - frances
                pt - português
                """;
        System.out.println(menuLivrosPorIdioma);
        var livroPorIdioma = leitura.nextLine();
        List<Livro> livrosIdioma = repositorio.findByIdiomaContainingIgnoreCase(livroPorIdioma);

        if (livrosIdioma.isEmpty()) {
            System.out.println("Não existem livros nesse idioma no banco de dados");
        } else {
            livrosIdioma.forEach(l -> System.out.println("*******************************" + "\nLivro: " + l.getTitulo() + "\nAutor: " + l.getAutor().getNome() + "\nIdioma: " + l.getIdioma() +
                    "\nNumero de downloads: " + l.getNumeroDownloads() + "\n*******************************\n"));
        }
    }
}