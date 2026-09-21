package com.example.curso.screen_match;

import com.example.curso.screen_match.model.DadosSerie;
import com.example.curso.screen_match.model.DadosTemporada;
import com.example.curso.screen_match.service.ConverteDados;
import org.junit.jupiter.api.Test;

public class ParseTest {
    @Test
    void parse() {
        var c = new ConverteDados();
        String serieJson = "{\"Title\":\"Gilmore Girls\",\"totalSeasons\":\"7\",\"imdbRating\":\"8.2\"}";
        String tempJson = "{\"Season\":\"1\",\"Episodes\":[{\"Title\":\"Pilot\",\"Episode\":\"1\",\"imdbRating\":\"8.0\",\"Released\":\"2000-10-05\"}]}";
        System.out.println("SERIE -> " + c.obterDados(serieJson, DadosSerie.class));
        System.out.println("TEMPORADA -> " + c.obterDados(tempJson, DadosTemporada.class));
    }
}
