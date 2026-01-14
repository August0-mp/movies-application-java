package com.ensta.myfilmlist.dao;

import com.ensta.myfilmlist.model.Film;
import com.ensta.myfilmlist.model.Realisateur;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

public interface RealisateurDAO {
    List<Realisateur> findAll();
    Optional<Realisateur> findById(long id);
    Realisateur findByNomAndPrenom(String nom, String prenom);
    Realisateur update (Realisateur realisateur);
    Realisateur save(Realisateur realisateur);
}
