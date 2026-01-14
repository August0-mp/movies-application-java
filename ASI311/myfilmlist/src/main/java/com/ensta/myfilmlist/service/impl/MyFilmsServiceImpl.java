package com.ensta.myfilmlist.service.impl;

import com.ensta.myfilmlist.dao.FilmDAO;
import com.ensta.myfilmlist.dao.RealisateurDAO;
import com.ensta.myfilmlist.dao.UtilisateurDAO;
import com.ensta.myfilmlist.dao.UtilisateurFilmDAO;
import com.ensta.myfilmlist.dto.FilmDTO;
import com.ensta.myfilmlist.dto.RealisateurDTO;
import com.ensta.myfilmlist.form.FilmForm;
import com.ensta.myfilmlist.form.RealisateurForm;
import com.ensta.myfilmlist.mapper.FilmMapper;
import com.ensta.myfilmlist.mapper.RealisateurMapper;
import com.ensta.myfilmlist.model.Film;
import com.ensta.myfilmlist.model.Realisateur;
import com.ensta.myfilmlist.model.Utilisateur;
import com.ensta.myfilmlist.model.UtilisateurFilm;
import com.ensta.myfilmlist.service.MyFilmsService;
import com.ensta.myfilmlist.exception.ServiceException;

import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class MyFilmsServiceImpl implements MyFilmsService {
    // Instanciation de FilmDAO
    private FilmDAO filmDAO;
    private RealisateurDAO realisateurDAO;
    private UtilisateurFilmDAO utilisateurFilmDAO;
    private UtilisateurDAO utilisateurDAO;

    public MyFilmsServiceImpl(){

    }

    @Autowired
    public MyFilmsServiceImpl(FilmDAO filmDAO, RealisateurDAO realisateurDAO, UtilisateurFilmDAO utilisateurFilmDAO, UtilisateurDAO utilisateurDAO) {
        this.filmDAO = filmDAO;
        this.realisateurDAO = realisateurDAO;
        this.utilisateurFilmDAO = utilisateurFilmDAO;
        this.utilisateurDAO = utilisateurDAO;
    }

    private void checkRealisateurOk(Realisateur realisateur) throws ServiceException {
        // Vérifier que le réalisateur n'est pas null
        if (realisateur == null) {
            throw new ServiceException("Le réalisateur ne doit pas etre null");
        }

        Optional<Realisateur> optionalRealisateur = realisateurDAO.findById(realisateur.getId());
        if (optionalRealisateur.isEmpty()) {
            throw new ServiceException("Ce realisateur n'existe pas");
        }
    }

    public List<FilmDTO> findAllFilmsOfRealisateur(Realisateur realisateur) throws ServiceException {
        this.checkRealisateurOk(realisateur); // throws an exception

        try {
            List<Film> films = filmDAO.findByRealisateurId(realisateur.getId());

            // Convertir les objets Film en FilmDTO
            List<FilmDTO> filmDTOs = new ArrayList<>();
            for (Film film : films) {
                if (film != null) {
                    filmDTOs.add(FilmMapper.convertFilmToFilmDTO(film)); // Conversion via FilmMapper
                }
            }
            return filmDTOs;
        } catch (Exception e) {
            throw new ServiceException("Une erreur est survenue lors de la recuperation des films du realisateur", e);
        }
    }

    @Override
    public Realisateur updateRealisateurCelebre(Realisateur realisateur) throws ServiceException {
        // Vérifier que le réalisateur n'est pas null
        if (realisateur == null) {
            throw new ServiceException("Le réalisateur ne doit pas etre null");
        }

        Optional<Realisateur> optionalRealisateur = realisateurDAO.findById(realisateur.getId());
        if (optionalRealisateur.isEmpty()) {
            throw new ServiceException("Ce realisateur n'existe pas");
        }

        try {
            List<Film> films = filmDAO.findByRealisateurId(realisateur.getId()) ;
            // Mettre à jour le statut "célèbre" en fonction du nombre de films réalisés
            boolean celebre = films.size() >= 3;
            realisateur.setCelebre(celebre);

            realisateurDAO.update(realisateur);

            // Retourner le réalisateur mis à jour
            return realisateur;
        } catch (Exception e) {
            // En cas d'erreur, lever une ServiceException
            throw new ServiceException("Une erreur est survenue lors de la mise à jour du statut célèbre.", e);
        }
    }

    public static List<Realisateur> updateRealisateurCelebres(List<Realisateur> realisateurs) throws ServiceException {
        // Vérifier que la liste des réalisateurs n'est pas nulle
        if (realisateurs == null) {
            throw new ServiceException("La liste des réalisateurs ne peut pas être nulle.");
        }

        try {
            // Utiliser Streams pour mettre à jour les réalisateurs célèbres et filtrer ceux qui sont célèbres
            return realisateurs.stream()
                    // Mettre à jour le statut "célèbre" pour chaque réalisateur en fonction du nombre de films
                    .peek(realisateur -> {
                        if (realisateur.getFilmRealises() != null) {
                            realisateur.setCelebre(realisateur.getFilmRealises().size() >= 3);
                        }
                    })
                    // Filtrer les réalisateurs célèbres
                    .filter(Realisateur::isCelebre)
                    .collect(Collectors.toList());  // Retourner une liste de réalisateurs célèbres
        } catch (Exception e) {
            // Lever une exception si une erreur survient
            throw new ServiceException("Une erreur est survenue lors de la mise à jour des réalisateurs célèbres.", e);
        }
    }

    @Override
    public int calculerDureeTotale(List<Film> films) {
        // Vérifier que la liste n'est pas nulle
        if (films == null) {
            throw new IllegalArgumentException("La liste des films ne peut pas être null.");
        }

        // Vérifier que la liste ne contient pas d'éléments nuls
        if (films.contains(null)) {
            throw new IllegalArgumentException("La liste des films ne peut pas contenir d'éléments null.");
        }

        // Calculer la somme des durées des films
        // Créer un Stream<Film>,
        // extraire les durées via mapToInt et sommer les durées avec sum
        return films.stream()
                .mapToInt(Film::getDuree)
                .sum();
    }

    @Override
    public double calculerNoteMoyenne(double[] notes) {
        // Vérifier que le tableau n'est pas null
        if (notes == null) {
            throw new IllegalArgumentException("Le tableau des notes ne peut pas être null.");
        }



        // Créer un Stream de doubles, calculer la moyenne et retourner le résultat arrondi
        OptionalDouble moyenneOptional = Arrays.stream(notes)  // Crée un DoubleStream à partir du tableau
                .average();     // Calcule la moyenne, renvoie un OptionalDouble

        // Si la moyenne est présente, arrondir la valeur, sinon retourner 0.0
        if (moyenneOptional.isPresent()) {
            // Arrondir la moyenne à 2 chiffres après la virgule
            return Math.round(moyenneOptional.getAsDouble() * 100.0) / 100.0;
        } else {
            return 0.0;  // Si aucune moyenne n'est présente (par exemple tableau vide), retourner 0.0
        }
    }


    @Override
    public List<FilmDTO> findAllFilms() throws ServiceException {
        try {
            // Appelle le DAO pour récupérer tous les films
            List<Film> films = filmDAO.findAll();

            // Convertir les objets Film en FilmDTO
            List<FilmDTO> filmDTOs = new ArrayList<>();
            for (Film film : films) {
                if (film != null) {
                    filmDTOs.add(FilmMapper.convertFilmToFilmDTO(film)); // Conversion via FilmMapper
                }
            }
            return filmDTOs;

        } catch (Exception e) {
            // En cas d'erreur, lever une ServiceException
            throw new ServiceException("Erreur lors de la récupération des films", e);
        }
    }

    @Override
    public FilmDTO createFilm(FilmForm filmForm) throws ServiceException {
        // Appelle le DAO pour récupérer tous les films
        Optional<Realisateur> realisateurOpt = realisateurDAO.findById(filmForm.getRealisateurId());

        if (realisateurOpt.isEmpty())
            throw new ServiceException("Realisateur de ce film n'existe pas.");

        try {
            Film film = FilmMapper.convertFilmFormToFilm(filmForm);
            Realisateur realisateur = realisateurOpt.get();
            film.setRealisateur(realisateur);   
            film = filmDAO.save(film);
            updateRealisateurCelebre(realisateur);
            return FilmMapper.convertFilmToFilmDTO(film);
        } catch (Exception e) {
            // En cas d'erreur, lever une ServiceException
            throw new ServiceException("Erreur lors de la création du film", e);
        }
    }

    @Override
    public RealisateurDTO createRealisateur(RealisateurForm realisateurForm) throws ServiceException {
        try {
            Realisateur realisateur = RealisateurMapper.convertRealisateurFormToRealisateur(realisateurForm);
            realisateur = realisateurDAO.save(realisateur);
            return RealisateurMapper.convertRealisateurToRealisateurDTO(realisateur);
        } catch (Exception e) {
            // En cas d'erreur, lever une ServiceException
            throw new ServiceException("Erreur lors de la création du realisateur", e);
        }
    }

    @Override
    public RealisateurDTO findRealisateurById(long id) throws ServiceException {
        try {
            return realisateurDAO.findById(id)
                    .map(RealisateurMapper::convertRealisateurToRealisateurDTO)
                    .orElse(null);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération du realisateur avec l'ID " + id, e);
        }
    }

    @Override
    public List<FilmDTO> findUserFavoriteFilms(long userId) throws ServiceException {
        try {
            List<Film> films = utilisateurFilmDAO.findByUserId(userId, true);

            List<FilmDTO> filmDTOs = new ArrayList<>();
            for (Film film : films) {
                if (film != null) {
                    filmDTOs.add(FilmMapper.convertFilmToFilmDTO(film)); // Conversion via FilmMapper
                }
            }
            return filmDTOs;
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération des films notés/favorités par l'utilisateur", e);
        }
    }

    @Override
    public Double findFilmAverageNote(long filmId) throws ServiceException {
        try {
            Optional<Double> optionalAvgNote = utilisateurFilmDAO.findFilmAverageNote(filmId);
            
            return optionalAvgNote.orElse(null);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de la moyenne de la note", e);
        }
    }

    @Override
    public Integer findFilmPersonalNote(long filmId, long userId) throws ServiceException {
        try {
            Optional<UtilisateurFilm> optionalPersonalNote = utilisateurFilmDAO.findByUserAndFilmId(userId, filmId);
            return optionalPersonalNote.map(UtilisateurFilm::getNote).orElse(null);

        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération de la note de l'utilisateur", e);
        }
    }

    private void setFilmFavorite(long filmId, long userId, boolean isFavorite) throws ServiceException {
        try {
            Optional<UtilisateurFilm> optUF = utilisateurFilmDAO.findByUserAndFilmId(userId, filmId);
            if (optUF.isPresent()) {
                UtilisateurFilm uf = optUF.get();
                uf.setIsFavorite(isFavorite);
                utilisateurFilmDAO.update(uf);
            } else {
                UtilisateurFilm uf = new UtilisateurFilm(userId, filmId, null, isFavorite);
                utilisateurFilmDAO.save(uf);
            }
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la mise à jour du film favoris", e);
        }
    }

    @Override
    public void addFilmToFavorite(long filmId, long userId) throws ServiceException {
        setFilmFavorite(filmId, userId, true);
    }

    @Override
    public void removeFilmFromFavorite(long filmId, long userId) throws ServiceException {
        setFilmFavorite(filmId, userId, false);
    }

    @Override
    public void evalFilm(long filmId, long userId, int note) throws ServiceException {
        try {
            Optional<UtilisateurFilm> optUF = utilisateurFilmDAO.findByUserAndFilmId(userId, filmId);
            if (optUF.isPresent()) {
                UtilisateurFilm uf = optUF.get();
                uf.setNote(note);
                utilisateurFilmDAO.update(uf);
            } else {
                UtilisateurFilm uf = new UtilisateurFilm(userId, filmId, note, null);
                utilisateurFilmDAO.save(uf);
            }
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la mise à jour du film favoris", e);
        }
    }

    @Override
    public Utilisateur login(String email, String password) throws ServiceException {
        Optional<Utilisateur> utilisateur = utilisateurDAO.findByEmail(email);
        if (utilisateur.isEmpty()) {
            throw new ServiceException("Utilisateur avec cet email n'existe pas");
        }
        if (!utilisateur.get().getPassword().equals(password)) {
            throw new ServiceException("Mot de passe incorrect");
        }
        return utilisateur.get();
    }

    @Override
    public Utilisateur signup(String email, String password, String prenom, String nom, boolean isAdmin) throws ServiceException {
        Optional<Utilisateur> utilisateurOpt = utilisateurDAO.findByEmail(email);
        if (utilisateurOpt.isPresent()) {
            throw new ServiceException("Utilisateur avec cet email existe déjà");
        }
        Utilisateur utilisateur = new Utilisateur(email, password, prenom, nom);
        return utilisateurDAO.save(utilisateur);
    }

    @Override
    public List<RealisateurDTO> findAllRealisateurs() throws ServiceException {
        try {
            // Appelle le DAO pour récupérer tous les films
            List<Realisateur> realisateurs = realisateurDAO.findAll();

            // Convertir les objets Film en FilmDTO
            List<RealisateurDTO> realisateurDTOS = new ArrayList<>();
            for (Realisateur realisateur : realisateurs) {
                if (realisateur != null) {
                    realisateurDTOS.add(RealisateurMapper.convertRealisateurToRealisateurDTO(realisateur)); // Conversion via FilmMapper
                }
            }
            return realisateurDTOS;
        } catch (Exception e) {
            // En cas d'erreur, lever une ServiceException
            throw new ServiceException("Erreur lors de la récupération des realisateurs", e);
        }
    }

    @Override
    public RealisateurDTO findRealisateurByNomAndPrenom(String nom, String prenom) throws ServiceException {
        Realisateur realisateur = realisateurDAO.findByNomAndPrenom(nom, prenom);

        return RealisateurMapper.convertRealisateurToRealisateurDTO(realisateur);
    }



    @Override
    public FilmDTO findFilmById(long id) throws ServiceException {
        try {
            return filmDAO.findById(id)
                    .map(FilmMapper::convertFilmToFilmDTO)
                    .orElse(null);
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la récupération du film avec l'ID " + id, e);
        }
    }
    @Override
    public void deleteFilm(long id) throws ServiceException {
        try {
            Optional<Film> optionalFilm = filmDAO.findById(id);
            if (optionalFilm.isPresent()) {
                Film film = optionalFilm.get();
                filmDAO.delete(film);
                updateRealisateurCelebre(film.getRealisateur());
            } else {
                throw new ServiceException("Film avec l'ID " + id + " non trouvé.");
            }
        } catch (Exception e) {
            throw new ServiceException("Erreur lors de la suppression du film avec l'ID " + id, e);
        }
    }


}
