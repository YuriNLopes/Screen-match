package com.example.curso.screen_match;

import com.example.curso.screen_match.model.DadosEpisodio;
import com.example.curso.screen_match.model.DadosSerie;
import com.example.curso.screen_match.model.DadosTemporada;
import com.example.curso.screen_match.principal.Principal;
import com.example.curso.screen_match.repository.SerieRepository;
import com.example.curso.screen_match.service.ConsumoApi;
import com.example.curso.screen_match.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.exibeMenu();
	}

}
