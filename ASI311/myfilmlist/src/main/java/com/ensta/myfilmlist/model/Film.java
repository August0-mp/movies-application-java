package com.ensta.myfilmlist.model;

public class Film {
	private long id;
	private String titre;
	private int duree;
	private Realisateur realisateur;

	public Film() {}
	public Film(String s, int i) {
		this.titre = s;
		this.duree = i;
	}

	// Getters and Setters
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }

	public String getTitre() { return titre; }
	public void setTitre(String titre) { this.titre = titre; }

	public int getDuree() { return duree; }
	public void setDuree(int duree) { this.duree = duree; }

	public Realisateur getRealisateur() { return realisateur; }
	public void setRealisateur(Realisateur realisateur) { this.realisateur = realisateur; }
}
