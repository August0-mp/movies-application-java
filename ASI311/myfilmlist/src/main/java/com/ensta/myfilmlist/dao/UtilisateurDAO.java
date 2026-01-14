package com.ensta.myfilmlist.dao;


import java.util.Optional;

import com.ensta.myfilmlist.model.Utilisateur;

public interface UtilisateurDAO {
    Optional<Utilisateur> findByEmail(String id);
    Utilisateur save(Utilisateur utilisateur);
}
