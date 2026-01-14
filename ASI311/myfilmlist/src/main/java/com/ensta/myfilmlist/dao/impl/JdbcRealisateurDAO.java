package com.ensta.myfilmlist.dao.impl;

import com.ensta.myfilmlist.dao.RealisateurDAO;
import com.ensta.myfilmlist.model.Film;
import com.ensta.myfilmlist.model.Realisateur;
import com.ensta.myfilmlist.persistence.ConnectionManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcRealisateurDAO implements RealisateurDAO {
    private static final String CREATE_REALISATEUR_QUERY = "INSERT INTO realisateur (prenom, nom, date_naissance, celebre) VALUES (?, ?, ?, ?)";
    private JdbcTemplate jdbcTemplate = ConnectionManager.getJdbcTemplate();

    public JdbcRealisateurDAO(){}

    @Override
    public List<Realisateur> findAll() {
        String sql = "SELECT * FROM realisateur";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Realisateur.class));
    }

    @Override
    public Optional<Realisateur> findById(long id) {
        String sql = "SELECT * FROM realisateur WHERE realisateur.id = ?";

        try {
            Realisateur realisateur = jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(Realisateur.class),
                    id
            );
            return Optional.ofNullable(realisateur);
        } catch (EmptyResultDataAccessException e) {
            // Aucun résultat trouvé, retourner un Optional vide
            return Optional.empty();
        }
    }

    @Override
    public Realisateur findByNomAndPrenom(String nom, String prenom) {
        String sql = "SELECT * FROM realisateur WHERE realisateur.prenom = ? AND realisateur.nom = ?";

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(Realisateur.class),
                    prenom, nom
            );
        } catch (EmptyResultDataAccessException e) {
            // Aucun résultat trouvé, retourner un Optional vide
            return null;
        }
    }

    @Override
    public Realisateur update(Realisateur realisateur) {
        String updateQuery = "UPDATE realisateur SET prenom = ?, nom = ?, date_naissance = ?, celebre = ? WHERE id = ?";
        jdbcTemplate.update(updateQuery,
                realisateur.getPrenom(),
                realisateur.getNom(),
                realisateur.getDateNaissance(),
                realisateur.isCelebre(),
                realisateur.getId()
        );
        return realisateur;
    }

    @Override
    public Realisateur save(Realisateur realisateur) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator creator = conn -> {
            PreparedStatement statement = conn.prepareStatement(CREATE_REALISATEUR_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, realisateur.getPrenom());
            statement.setString(2, realisateur.getNom());
            statement.setDate(3, Date.valueOf(realisateur.getDateNaissance()));
            statement.setBoolean(4, realisateur.isCelebre());
            return statement;
        };
        jdbcTemplate.update(creator, keyHolder);
        realisateur.setId(keyHolder.getKey().longValue());
        return realisateur;
    }

}
