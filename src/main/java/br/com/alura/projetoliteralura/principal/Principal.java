package br.com.alura.projetoliteralura.principal;

import br.com.alura.projetoliteralura.modelos.*;
import br.com.alura.projetoliteralura.repositorios.LivrosRepositorio;
import br.com.alura.projetoliteralura.reutilizaveis.ConsumoApi;
import br.com.alura.projetoliteralura.reutilizaveis.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados converte = new ConverteDados();
    private LivrosRepositorio repositorio;
    private List<Livro> livro = new ArrayList<>();
    private List<Autor> autor = new ArrayList<>();


    public Principal(LivrosRepositorio repositorio) {
        this.repositorio = repositorio;
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

            if (repositorio.existsByTitulo(livroLocalizado.getTitulo())){
                System.out.println("Livro já registrado!");
                return;
            }

            repositorio.save(livroLocalizado);
            System.out.println("**********************************************************");
            System.out.println("Titulo: " + livroLocalizado.getTitulo() + "\nAutor: "+ livroLocalizado.getAutor().getNome() + "\nIdioma: " + livroLocalizado.getIdioma() + "\nNúmero de downloads: " + livroLocalizado.getNumeroDownloads());
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
        livro = repositorio.findAll();
        livro.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(a -> System.out.println("\nAutor: " + a.getAutor().getNome() + "\nAno de nascimento: " +
                        a.getAutor().getAnoDeNascimento() + "\nAno de falecimento: " + a.getAutor().getAnoDeFalecimento() + "\nLivros: ["
                        + livro.stream()
                                .filter(l -> l.getAutor().getNome().equals(a.getAutor().getNome()))
                        .map(Livro::getTitulo)
                        .collect(Collectors.joining(", ")) + "]"));
    }

    private void listarAutoresVivosPorEpoca(){
        System.out.println("Digite o nome do autor desejado: ");
        var autorPorEpoca = leitura.nextLine();
        List<Autor> autorEncontrado = repositorio.autoresVivoPorEpoca(autorPorEpoca);
        autorEncontrado.stream()
                .filter(a -> a.getNome().equals(a.getNome()))
                .forEach(System.out::println);
    }
}