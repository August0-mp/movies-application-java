package com.ensta.myfilmlist.form;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import org.junit.experimental.theories.DataPoint;
import org.springframework.lang.NonNull;

public class RealisateurForm {
    @NotBlank
    private String prenom;
    
    @NotBlank
    private String nom;
    
    @NonNull
    private LocalDate dateNaissance;

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
}
