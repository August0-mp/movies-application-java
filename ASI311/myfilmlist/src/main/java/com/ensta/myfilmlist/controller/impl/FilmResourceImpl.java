package com.ensta.myfilmlist.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensta.myfilmlist.controller.FilmResource;
import com.ensta.myfilmlist.dto.FilmDTO;
import com.ensta.myfilmlist.exception.ControllerException;
import com.ensta.myfilmlist.form.FilmForm;
import com.ensta.myfilmlist.service.MyFilmsService;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

import java.util.List;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/film")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from your React app
public class FilmResourceImpl implements FilmResource {

    @Autowired
    private MyFilmsService myFilmsService;

    @ApiOperation(value = "Lister les films", notes = "Permet de renvoyer la liste de tous les films.", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des films a été renvoyée correctement")
    })
    @Override
    public ResponseEntity<List<FilmDTO>> getAllfilms() throws ControllerException {
        try {
            List<FilmDTO> films = myFilmsService.findAllFilms();
            return ResponseEntity.ok(films);
        } catch (Exception e) {
            throw new ControllerException("Error in searching for films.", e);
        }
    }

    @ApiOperation(value = "Liste un film par son ID", notes = "Permet de renvoyer un film par son ID.", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le film a été renvoyé correctement"),
            @ApiResponse(code = 404, message = "Le film n'a pas été trouvé")
    })
    @Override
    public ResponseEntity<FilmDTO> getFilmById(long ID) throws ControllerException {
        try {
            FilmDTO film = myFilmsService.findFilmById(ID);

            if (film == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(film);
        } catch (Exception e) {
            throw new ControllerException("Error in searching for films.", e);
        }
    }

    @ApiOperation(value = "Cree un nouveau film", notes = "Cree un nouveau film en passant ses attributs par le body.", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Le film a été crée correctement"),
            @ApiResponse(code = 400, message = "Le film n'a pas pu etre cree")
    })
    @Override
    @Transactional
    public ResponseEntity<FilmDTO> createFilm(@RequestBody FilmForm filmForm) throws ControllerException {
        try {
            FilmDTO createdFilm = myFilmsService.createFilm(filmForm);

            return ResponseEntity.status(201).body(createdFilm);
        } catch (Exception e) {
            throw new ControllerException("Error in creating the film.", e);
        }
    }

    @ApiOperation(value = "Supprime un film par son ID", notes = "Permet de supprimer un film par son ID.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Le film a été supprimé correctement"),
            @ApiResponse(code = 404, message = "Le film n'a pas été trouvé")
    })
    @Override
    @Transactional
    public ResponseEntity<?> deleteFilm(long ID) throws ControllerException {
        try {
            FilmDTO film = myFilmsService.findFilmById(ID);

            if (film == null) {
                return ResponseEntity.notFound().build();
            }

            myFilmsService.deleteFilm(film.getId());

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ControllerException("Error in deleting film.", e);
        }
    }

    @ApiOperation(value = "Ajoute un film a la liste de favorits par filmId et userId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Film added to favorites!"),
            @ApiResponse(code = 404, message = "Error in adding film to favorites.")
    })
    @Override
    public ResponseEntity<String> addFilmToFavorite(long filmID, long userId) throws ControllerException {
        try {
            FilmDTO film = myFilmsService.findFilmById(filmID);

            if (film == null) {
                return ResponseEntity.notFound().build();
            }else{
                myFilmsService.addFilmToFavorite(filmID, userId);
            }

            return ResponseEntity.ok("Film added to favorits!");
        } catch (Exception e) {
            throw new ControllerException("Error in adding film to favorites.", e);
        }
    }

    
    @ApiOperation(value = "Liste les filmes préférés d'un utilisateur par son ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des films préférés a été renvoyée correctement"),
            @ApiResponse(code = 404, message = "Error in searching for favorite user films.")
    })
    @Override
    public ResponseEntity<List<FilmDTO>> findUserFavoriteFilms(long userId) throws ControllerException {
        try {
            List<FilmDTO> films = myFilmsService.findUserFavoriteFilms(userId);
            
            return ResponseEntity.ok(films);
        } catch (Exception e) {
            throw new ControllerException("Error in searching for favorite user films.", e);
        }
    }

    
    @ApiOperation(value = "Supprime un film de la liste de favorits par filmId et userId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Film removed to favorites!"),
            @ApiResponse(code = 404, message = "Error in removing film from favorites.")
    })
    @Override
    public ResponseEntity<String> removeFilmToFavorite(long filmID, long userId) throws ControllerException {
        try {
            FilmDTO film = myFilmsService.findFilmById(filmID);

            if (film == null) {
                return ResponseEntity.notFound().build();
            }else{
                myFilmsService.removeFilmFromFavorite(filmID, userId);
            }

            return ResponseEntity.ok("Film removed from favorits!");
        } catch (Exception e) {
            throw new ControllerException("Error in removing film from favorites.", e);
        }
    }

    @ApiOperation(value = "Vérifie si un film est un favori d'un utilisateur par filmId et userId")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Renvoie true si le film est favori, sinon false"),
            @ApiResponse(code = 500, message = "Erreur lors de la vérification du statut favori")
    })
    @Override
    public ResponseEntity<Boolean> isFilmAFavorite(long filmID, long userId) throws ControllerException {
        try {
            List<FilmDTO> films = myFilmsService.findUserFavoriteFilms(userId);
            
            boolean isFavorite = films.stream().anyMatch(film -> film.getId() == filmID);
            return ResponseEntity.ok(isFavorite);
    
        } catch (Exception e) {
            throw new ControllerException("Error in searching for favorite user films.", e);
        }
    }

    @ApiOperation(value = "Récupérer la note moyenne d'un film")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La note moyenne du film a été renvoyée correctement"),
            @ApiResponse(code = 404, message = "Film non trouvé")
    })
    @Override
    public ResponseEntity<Double> findFilmAverageNote(long filmId) throws ControllerException {
        try {
            Double averageNote = myFilmsService.findFilmAverageNote(filmId);
            return ResponseEntity.ok(averageNote);
        } catch (Exception e) {
            throw new ControllerException("Error retrieving film average note.", e);
        }
    }

    @ApiOperation(value = "Récupérer la note personnelle d'un utilisateur pour un film")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La note personnelle a été renvoyée correctement"),
            @ApiResponse(code = 404, message = "Film ou note non trouvé")
    })
    @Override
    public ResponseEntity<Integer> findFilmPersonalNote(long filmId, long userId) throws ControllerException {
        try {
            Integer personalNote = myFilmsService.findFilmPersonalNote(filmId, userId);
            return ResponseEntity.ok(personalNote);
        } catch (Exception e) {
            throw new ControllerException("Error retrieving personal note for film.", e);
        }
    }

    @ApiOperation(value = "Évaluer un film")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Le film a été évalué correctement"),
            @ApiResponse(code = 400, message = "Note invalide"),
            @ApiResponse(code = 404, message = "Film non trouvé")
    })
    @Override
    public ResponseEntity<String> evalFilm(long filmId, long userId, int note) throws ControllerException {
        try {
            myFilmsService.evalFilm(filmId, userId, note);
            return ResponseEntity.ok("Film évalué avec succès!");
        } catch (Exception e) {
            throw new ControllerException("Error evaluating film.", e);
        }
    }




}
