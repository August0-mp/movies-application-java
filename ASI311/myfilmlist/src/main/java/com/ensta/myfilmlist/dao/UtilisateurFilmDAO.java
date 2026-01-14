package com.ensta.myfilmlist.dao;

import com.ensta.myfilmlist.model.Film;
import com.ensta.myfilmlist.model.UtilisateurFilm;

import java.util.List;
import java.util.Optional;

public interface UtilisateurFilmDAO {
    List<Film> findByUserId(long userId, boolean includeFavorite);
    Optional<UtilisateurFilm> findByUserAndFilmId(long userId, long filmId);
    Optional<Double> findFilmAverageNote(long filmId);
    UtilisateurFilm save(UtilisateurFilm utilisateurFilm);
    UtilisateurFilm update(UtilisateurFilm utilisateurFilm);
}
