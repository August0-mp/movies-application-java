package com.ensta.myfilmlist.dao.impl;

import com.ensta.myfilmlist.dao.UtilisateurFilmDAO;
import com.ensta.myfilmlist.model.Film;
import com.ensta.myfilmlist.model.UtilisateurFilm;
import com.ensta.myfilmlist.persistence.ConnectionManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUtilisateurFilmDAO implements UtilisateurFilmDAO {
    private static final String CREATE_UTILISATEUR_FILM_QUERY = "INSERT INTO UtilisateurFilm (utilisateur_id, film_id, note, is_favorite) VALUES (?, ?, ?, ?)";
    private JdbcTemplate jdbcTemplate = ConnectionManager.getJdbcTemplate();
    @Override
    public List<Film> findByUserId(long userId, boolean includeFavorite) {
        String sql = "SELECT f.id AS film_id, f.titre AS film_titre, f.duree AS film_duree, " +
                "r.id AS realisateur_id, r.nom AS realisateur_nom, r.prenom AS realisateur_prenom, " +
                "r.date_naissance AS realisateur_date_naissance, r.celebre AS realisateur_celebre " +
                "FROM Film f " +
                "INNER JOIN Realisateur r ON f.realisateur_id = r.id " +
                "INNER JOIN UtilisateurFilm uf ON uf.film_id = f.id " +
                "WHERE uf.utilisateur_id = ?" + (includeFavorite ? " AND uf.is_favorite = true " : "");

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> JdbcFilmDAO.getFilmFrom(rs));
    }

    @Override
    public Optional<UtilisateurFilm> findByUserAndFilmId(long userId, long filmId) {
        String sql = "SELECT * FROM UtilisateurFilm WHERE utilisateur_id = ? AND film_id = ?";

        try {
            UtilisateurFilm utilisateurFilm = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{userId, filmId},
                    (rs, rowNum) -> new UtilisateurFilm(
                            rs.getLong("utilisateur_id"),
                            rs.getLong("film_id"),
                            rs.getObject("note", Integer.class),      // Returns an Integer, handling null values
                            rs.getObject("is_favorite", Boolean.class)  // Returns a Boolean, handling null values
                    )
            );
            return Optional.of(utilisateurFilm);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Double> findFilmAverageNote(long filmId) {
        String sql = "SELECT AVG(CAST(uf.note AS DOUBLE PRECISION)) as average_rating " +
                "FROM UtilisateurFilm uf " +
                "WHERE uf.film_id = ? AND uf.note IS NOT NULL";
                    
                Double average = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{filmId},
                    (rs, rowNum) -> rs.getObject("average_rating", Double.class)
                    );

        return Optional.ofNullable(average);
    }

    public UtilisateurFilm save(UtilisateurFilm utilisateurFilm) {
        PreparedStatementCreator creator = conn -> {
            PreparedStatement statement = conn.prepareStatement(CREATE_UTILISATEUR_FILM_QUERY);
            statement.setLong(1, utilisateurFilm.getUserId());
            statement.setLong(2, utilisateurFilm.getFilmId());

            // Handle nullable fields properly
            if (utilisateurFilm.getNote() != null) {
                statement.setInt(3, utilisateurFilm.getNote());
            } else {
                statement.setNull(3, Types.INTEGER);
            }

            if (utilisateurFilm.isFavorite() != null) {
                statement.setBoolean(4, utilisateurFilm.isFavorite());
            } else {
                statement.setNull(4, Types.BOOLEAN);
            }

            return statement;
        };

        jdbcTemplate.update(creator);
        return utilisateurFilm; // Return the object without setting an auto-generated ID
    }

    @Override
    public UtilisateurFilm update(UtilisateurFilm utilisateurFilm) {
        String sql = "UPDATE UtilisateurFilm SET note = ?, is_favorite = ? WHERE utilisateur_id = ? AND film_id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql);

            // Handle nullable fields properly
            if (utilisateurFilm.getNote() != null) {
                statement.setInt(1, utilisateurFilm.getNote());
            } else {
                statement.setNull(1, Types.INTEGER);
            }

            if (utilisateurFilm.isFavorite() != null) {
                statement.setBoolean(2, utilisateurFilm.isFavorite());
            } else {
                statement.setNull(2, Types.BOOLEAN);
            }

            statement.setLong(3, utilisateurFilm.getUserId());
            statement.setLong(4, utilisateurFilm.getFilmId());

            return statement;
        });

        return utilisateurFilm;
    }
}
