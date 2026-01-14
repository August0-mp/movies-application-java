package com.ensta.myfilmlist.model;

public class UtilisateurFilm {
    private long userId;
    private long filmId;
    private Integer note;
    private Boolean isFavorite;

    public UtilisateurFilm(long userId, long filmId,Integer note,Boolean isFavorite) {
        this.userId = userId;
        this.filmId = filmId;
        this.note = note;
        this.isFavorite = isFavorite;
    }

    public Integer getNote() {
        return note;
    }
    public void setNote(Integer note) {
        this.note = note;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFilmId() {
        return filmId;
    }
    public void setFilmId(long filmId) {
        this.filmId = filmId;
    }

    public Boolean isFavorite() {
        return isFavorite;
    }
    public void setIsFavorite(Boolean favorite) {
        isFavorite = favorite;
    }
}
