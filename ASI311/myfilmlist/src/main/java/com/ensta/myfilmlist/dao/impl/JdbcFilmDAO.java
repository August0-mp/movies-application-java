package com.ensta.myfilmlist.dao.impl;

import com.ensta.myfilmlist.dao.FilmDAO;
import com.ensta.myfilmlist.model.Film;
import com.ensta.myfilmlist.persistence.ConnectionManager;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ensta.myfilmlist.model.Realisateur;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository
public class JdbcFilmDAO implements FilmDAO {
    private static final String CREATE_FILM_QUERY = "INSERT INTO film (titre, duree, realisateur_id) VALUES (?, ?, ?)";
    private JdbcTemplate jdbcTemplate = ConnectionManager.getJdbcTemplate();

    public JdbcFilmDAO(){}

    @Override
    public List<Film> findAll() {
        String sql =
                "SELECT f.id AS film_id, " +
                        "f.titre AS film_titre, " +
                        "f.duree AS film_duree, " +
                        "r.id AS realisateur_id, " +
                        "r.nom AS realisateur_nom, " +
                        "r.prenom AS realisateur_prenom, " +
                        "r.date_naissance AS realisateur_date_naissance, " +
                        "r.celebre AS realisateur_celebre " +
                        "FROM film f " +
                        "INNER JOIN realisateur r " +
                        "ON f.realisateur_id = r.id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> getFilmFrom(rs));
    }
    @Override
    public Film save(Film film) {   
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator creator = conn -> {
            PreparedStatement statement = conn.prepareStatement(CREATE_FILM_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, film.getTitre());
            statement.setInt(2, film.getDuree());
            statement.setLong(3, film.getRealisateur().getId());
            return statement;
        };
        jdbcTemplate.update(creator, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        return film;
    }


    @Override
    public Optional<Film> findById(long id) {
        String sql = "SELECT f.id AS film_id, f.titre AS film_titre, f.duree AS film_duree, " +
                "r.id AS realisateur_id, r.nom AS realisateur_nom, r.prenom AS realisateur_prenom, " +
                "r.date_naissance AS realisateur_date_naissance, r.celebre AS realisateur_celebre " +
                "FROM film f " +
                "INNER JOIN realisateur r ON f.realisateur_id = r.id " +
                "WHERE f.id = ?";

        return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
            if (rs.next()) {
                Film film = getFilmFrom(rs);
                return Optional.of(film);
            }
            return Optional.empty();
        });
    }
    @Override
    public void delete(Film film) {
        String sql = "DELETE FROM film WHERE id = ?";
        jdbcTemplate.update(sql, film.getId());
    }
    @Override
    public List<Film> findByRealisateurId(long realisateurId) {
        String sql = "SELECT f.id AS film_id, f.titre AS film_titre, f.duree AS film_duree, " +
                "r.id AS realisateur_id, r.nom AS realisateur_nom, r.prenom AS realisateur_prenom, " +
                "r.date_naissance AS realisateur_date_naissance, r.celebre AS realisateur_celebre " +
                "FROM film f " +
                "INNER JOIN realisateur r ON f.realisateur_id = r.id " +
                "WHERE r.id = ?";

        return jdbcTemplate.query(sql, new Object[]{realisateurId}, (rs, rowNum) -> getFilmFrom(rs));
    }
    static Film getFilmFrom(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film_id"));
        film.setTitre(rs.getString("film_titre"));
        film.setDuree(rs.getInt("film_duree"));

        Realisateur realisateur = new Realisateur();
        realisateur.setId(rs.getLong("realisateur_id"));
        realisateur.setNom(rs.getString("realisateur_nom"));
        realisateur.setPrenom(rs.getString("realisateur_prenom"));
        realisateur.setDateNaissance(rs.getDate("realisateur_date_naissance").toLocalDate());
        realisateur.setCelebre(rs.getBoolean("realisateur_celebre"));

        film.setRealisateur(realisateur);
        return film;
    }
}
