package com.example.curso.screen_match;

import com.example.curso.screen_match.model.DadosEpisodio;
import com.example.curso.screen_match.model.DadosSerie;
import com.example.curso.screen_match.model.DadosTemporada;
import com.example.curso.screen_match.principal.Principal;
import com.example.curso.screen_match.service.ConsumoApi;
import com.example.curso.screen_match.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		var consumoApi = new ConsumoApi();
//		var json = consumoApi.obterDados("https://omdbapi.com/?t=gilmore+girls&apikey=d6027244");
//		System.out.println(json);
//		ConverteDados conversor = new ConverteDados();
//		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
//
//		System.out.println("Titulo: " + dados.titulo());
//		json = consumoApi.obterDados("https://omdbapi.com/?t=gilmore+girls&season=1&episode=1&apikey=d6027244");
//		DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
//		System.out.println("Titulo: " + dadosEpisodio);
//		List<DadosTemporada> temporadas = new ArrayList<>();
//
//		for (int i =1; i<= dados.totalTemporadas(); i++){
//			json = consumoApi.obterDados("https://omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=d6027244");
//			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
//			temporadas.add(dadosTemporada);
//		}
//		temporadas.forEach(System.out::println);    //codigo realocado para classe Principal separada

		Principal principal = new Principal();
		principal.exibeMenu();
	}

}
