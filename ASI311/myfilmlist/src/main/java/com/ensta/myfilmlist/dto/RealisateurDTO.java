package com.ensta.myfilmlist.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RealisateurDTO {
    private long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private List<FilmDTO> filmRealises;
    private boolean celebre;

    //constructeurs
    public RealisateurDTO() {};
    public RealisateurDTO(long id, String nom, String prenom, LocalDate dateNaissance, boolean celebre) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.celebre = celebre;
        this.filmRealises = new ArrayList<>();

    }
    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public List<FilmDTO> getFilmRealises() { return filmRealises; }
    public void setFilmRealises(List<FilmDTO> filmRealises) { this.filmRealises = filmRealises; }

    public boolean isCelebre() { return celebre; }
    public void setCelebre(boolean celebre) { this.celebre = celebre; }

    @Override
    public String toString() {
        return "RealisateurDTO [id=" + id + ", prenom=" + prenom + ", nom=" + nom + ", date_naissance=" + dateNaissance + ", celebre=" + celebre + "]";
    }
}
