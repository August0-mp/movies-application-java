package com.ensta.myfilmlist.dao.impl;

import com.ensta.myfilmlist.dao.UtilisateurDAO;
import com.ensta.myfilmlist.model.Utilisateur;
import com.ensta.myfilmlist.persistence.ConnectionManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class JbdcUtilisateurDAO implements UtilisateurDAO {

    private static final String CREATE_UTILISATEUR_QUERY = "INSERT INTO Utilisateur (email, password, nom, prenom, is_admin) VALUES (?, ?, ?, ?, ?)";
    private JdbcTemplate jdbcTemplate = ConnectionManager.getJdbcTemplate();


    // Injecter le PasswordEncoder via le constructeur
    public JbdcUtilisateurDAO() {}

    public Optional<Utilisateur> findByEmail(String email) {
        String sql = "SELECT * FROM utilisateur WHERE utilisateur.email = ?";

        try {
            Utilisateur utilisateur = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Utilisateur user = new Utilisateur();
                user.setId(rs.getLong("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPrenom(rs.getString("prenom"));
                user.setNom(rs.getString("nom"));
                user.setAdmin(rs.getBoolean("is_admin")); // Ensure proper mapping
                return user;
            }, email);

            return Optional.ofNullable(utilisateur);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator creator = conn -> {
            PreparedStatement statement = conn.prepareStatement(CREATE_UTILISATEUR_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, utilisateur.getEmail());
            statement.setString(2, utilisateur.getPassword()); // Mot de passe encodé
            statement.setString(3, utilisateur.getNom());
            statement.setString(4, utilisateur.getPrenom());
            statement.setBoolean(5, utilisateur.isAdmin());
            return statement;
        };
        jdbcTemplate.update(creator, keyHolder);
        utilisateur.setId(keyHolder.getKey().longValue());
        return utilisateur;
    }
}
