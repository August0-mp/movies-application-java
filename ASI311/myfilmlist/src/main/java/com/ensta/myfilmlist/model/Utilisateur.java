package com.ensta.myfilmlist.model;

public class Utilisateur {
    private long id;
    private String email;
    private String password;
    private String prenom;
    private String nom;
    private boolean isAdmin;

    public Utilisateur() {}

    public Utilisateur(String email, String password, String prenom, String nom) {
        this.email = email;
        this.password = password;
        this.prenom = prenom;
        this.nom = nom;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
}
