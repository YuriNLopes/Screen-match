package com.example.curso.screen_match.repository;

import com.example.curso.screen_match.model.Categoria;
import com.example.curso.screen_match.model.Episodio;
import com.example.curso.screen_match.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);
    List<Serie> findByAtoresConainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double AvaliacaoMinima);

    List<Serie> FindTop5OrderByAvaliacaoDesc();

    List<Serie> FindByGenero(Categoria categoria);

    @Query("select s from Series s WHERE s.total_temporadas <= :maxTemporadas AND s.avaliacao >= :minAvaliacao")
    List<Serie> seriePorTemporadaEAvaliacao(int maxTemporadas, double minAvaliacao);

    @Query("select e from Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodioPorTrecho(String trechoEpisodio);

    @Query("select e from Serie s JOIN s.episodios e WHERE s = :serie ORDER BY avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("select e from Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}
