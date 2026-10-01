package com.example.curso.screen_match.principal;

import com.example.curso.screen_match.model.*;
import com.example.curso.screen_match.repository.SerieRepository;
import com.example.curso.screen_match.service.ConsumoApi;
import com.example.curso.screen_match.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private ConsumoApi consumoApi = new ConsumoApi();

    private ConverteDados conversor = new ConverteDados();
    private Scanner leitura = new Scanner(System.in);

    private final String ENDERECO_API = "http://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=d6027244";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBusca;
    private Object serie;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {

            var menu = """
                    1 - Buscar Serie
                    2 - Buscar Episodios
                    3 - Listar Serie buscadas
                    4 - Buscar serie por titulo
                    5 - Buscar serie por ator
                    6 - Top5 Series
                    7 - Busca por serie categoria
                    8 - Filtrar serie
                    9 - Buscar episodio por trecho
                    10 - buscar top 5 episodios por serie
                    11 - buscar episodio a partir da data
                                        
                    0 - Sair                
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSerieBuscada();
                    break;
                case 4:
                    BuscarSeriePorTitulo();
                    break;
                case 5:
                    BuscarSeriePorAtor();
                    break;
                case 6:
                    BuscarTopSeries();
                    break;
                case 7:
                    BuscarSeriePorCategoria();
                case 8:
                    FiltrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodioPorSerie();
                    break;
                case 11:
                    buscarEpisodioAPartirDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarEpisodioAPartirDeUmaData() {
        BuscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie= serieBusca.get();
            System.out.println("Digite o ano de lancamento");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie,anoLancamento);
        }
    }

    private void topEpisodioPorSerie() {
        BuscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Serie %s temporada %s - Episodio %s -%s\n",
                            e.getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(),e.getTitulo()));

        }


    }

    private void FiltrarSeriesPorTemporadaEAvaliacao() {
        System.out.println("Filtrar serie ate quantas temporadas?");
        var maxTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Filtrar serie a partir de quanto de avaliacao?");
        var minAvaliacao = leitura.nextInt();
        leitura.nextLine();
        List<Serie> filtroSerie = repositorio.seriePorTemporadaEAvaliacao(maxTemporadas,minAvaliacao);
        System.out.println("Series Filtradas--");
        filtroSerie.forEach( s->
                System.out.println(s.getTitulo() + "  avaliacao : "+s.getAvaliacao()));

    }

    private void BuscarSeriePorCategoria() {
        System.out.println("Digite a categoria para busca");
        var nomeCategoria = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeCategoria);
        List<Serie> seriePorCategoria = repositorio.FindByGenero(categoria);
        System.out.println("Series da categoria "+nomeCategoria);
        seriePorCategoria.forEach(System.out::println);

    }

    private void BuscarTopSeries() {
        List<Serie> serieTop = repositorio.FindTop5OrderByAvaliacaoDesc();
        serieTop.forEach(s -> System.out.println(s.getTitulo()+ " avalicao "+s.getAvaliacao()));
    }

    private void BuscarSeriePorAtor() {
        System.out.println("Qual o nome para busca?");
        var nomeAtor = leitura.nextLine();
        System.out.println("Qual a avaliacao minima");
        var avaliacaoMin = leitura.nextDouble();


        List<Serie> seriesEncontradas = repositorio.findByAtoresConainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor,avaliacaoMin);
        System.out.println("Series em que "+nomeAtor+"  trabalhou:");
        seriesEncontradas.forEach( s -> System.out.println(s.getTitulo() + " avaliacao " +s.getAvaliacao()));
    }

    private void BuscarSeriePorTitulo() {
        System.out.println("Escolha uma serie registrada:");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()) {
            System.out.println("Dados da serie: "+ serieBusca.get());
        } else {
            System.out.println("Serie nao encontrada");
        }

    }

    private void listarSerieBuscada() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
//        dadosSeries.add(dados);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da serie par abusca");
        var nomeSerie = leitura.nextLine();
        var nomeSerieFormatado = nomeSerie.replace(" ", "+");
        var json = consumoApi.obterDados(ENDERECO_API + nomeSerieFormatado + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        System.out.println("Escolha uma serie registrada:");
        listarSerieBuscada();
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obterDados(ENDERECO_API + serieEncontrada.getTitulo() + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios =temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Serie nao encontrada");
        }

    }
    private void buscarEpisodioPorTrecho(){
        System.out.println("Qual o nome do episodio para busca");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodioPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie %s temporada %s - Episodio %s -%s\n",
                            e.getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(),e.getTitulo()));


    }
}
