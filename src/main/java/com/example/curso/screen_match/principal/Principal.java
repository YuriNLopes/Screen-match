package com.example.curso.screen_match.principal;

import com.example.curso.screen_match.model.DadosEpisodio;
import com.example.curso.screen_match.model.DadosSerie;
import com.example.curso.screen_match.model.DadosTemporada;
import com.example.curso.screen_match.model.Episodio;
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
    public void exibeMenu() {
        System.out.println("Bem-vindo ao Screen Match!");
        System.out.println("Digite o nome da serie");
        var nomeSerie = leitura.nextLine();
        var nomeSerieFormatado = nomeSerie.replace(" ", "+");
//        var json = consumoApi.obterDados(ENDERECO_API + nomeSerie.replace(" ", "+") + API_KEY);
        var json = consumoApi.obterDados(ENDERECO_API + nomeSerieFormatado + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumoApi.obterDados(ENDERECO_API + nomeSerieFormatado + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

//        for(int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j =0; j < episodiosTemporada.size(); j++) {
//                System.out.println("Episodio: " + episodiosTemporada.get(j).titulo());
//            }
//        } //refatorando o código acima para usar lambda

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println("Episodio: " + e.titulo())));

//    List<String> nomes = Arrays.asList("maria", "yuri", "joao", "ana");
//    nomes.stream()
//            .sorted()
//            .limit(3)
//            .filter(n -> n.startsWith("a"))
//            .map(String::toUpperCase)
//            .forEach(System.out::println);

        List<DadosEpisodio> dadosEpisodioLista = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        dadosEpisodioLista.stream().sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .filter(e -> !e.avaliacao().equals("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro: " + e.titulo() + " - Avaliação: " + e.avaliacao()))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenacao: " + e.titulo() + " - Avaliação: " + e.avaliacao()))
                .limit(10)
                .map(e -> e.titulo().toUpperCase())
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Digite o trecho do titulo que deseja buscar");
        var trechoTitulo = leitura.nextLine();

        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if (episodioBuscado.isPresent()) {
            System.out.println("Episodio encontrado: " + episodioBuscado.get());
        } else {
            System.out.println("Nenhum episodio encontrado com o trecho informado.");
        }

        System.out.println(episodioBuscado);

        System.out.println("Digite o nome do episodio que deseja buscar");
        int ano = Integer.parseInt(leitura.nextLine());

        leitura.nextLine();

//        LocalDate dataBusca = LocalDate.of(ano,1,1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                        " Episodio: " + e.getTitulo() +
//                        " Data de lançamento: " + e.getDataLancamento().format(formatador) +
//                        " Avaliação: " + e.getAvaliacao()
//                ));
//
        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println("Média de avaliação por temporada:" + avaliacaoPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() != null && e.getAvaliacao() > 0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Estatísticas de avaliação:" + est.getAverage() +
                            " - maxima avaliacao "+ est.getMax() +
                            " - minima avaliacao " + est.getMin() +
                            " - total de episodios avaliados " + est.getCount());
    }
    }

