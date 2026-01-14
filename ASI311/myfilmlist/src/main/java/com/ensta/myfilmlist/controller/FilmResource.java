package com.ensta.myfilmlist.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ensta.myfilmlist.dto.FilmDTO;
import com.ensta.myfilmlist.exception.ControllerException;
import com.ensta.myfilmlist.form.FilmForm;

import io.swagger.annotations.Api;
import io.swagger.models.Response;
import io.swagger.v3.oas.annotations.tags.Tag;

@Api(tags = "Film")
@Tag(name = "Film", description = "Opération sur les films")
public interface FilmResource {

    @GetMapping
    ResponseEntity<List<FilmDTO>> getAllfilms() throws ControllerException;
    
    @GetMapping("/{id}")
    ResponseEntity<FilmDTO> getFilmById(@Valid @PathVariable("id") long id) throws ControllerException;

    @PostMapping
    ResponseEntity<FilmDTO> createFilm(@Valid @RequestBody FilmForm filmForm) throws ControllerException;

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteFilm(@Valid @PathVariable("id") long id) throws ControllerException;

    @GetMapping("/favorite/{userId}")
    ResponseEntity<List<FilmDTO>> findUserFavoriteFilms(@PathVariable("userId") long userId) throws ControllerException;
    
    @PostMapping("/favorite/{filmId}/{userId}")
    ResponseEntity<String> addFilmToFavorite(@PathVariable("filmId") long filmId, @PathVariable("userId") long userId) throws ControllerException;

    @GetMapping("/favorite/{filmId}/{userId}")
    ResponseEntity<Boolean> isFilmAFavorite(@PathVariable("filmId") long filmId, @PathVariable("userId") long userId) throws ControllerException;

    @DeleteMapping("/favorite/{filmId}/{userId}")
    ResponseEntity<String> removeFilmToFavorite(@PathVariable("filmId") long filmId, @PathVariable("userId") long userId) throws ControllerException;

    @GetMapping("/note/average/{filmId}")
    ResponseEntity<Double> findFilmAverageNote(@PathVariable("filmId") long filmId) throws ControllerException;

    @GetMapping("/note/personal/{filmId}/{userId}")
    ResponseEntity<Integer> findFilmPersonalNote(@PathVariable("filmId") long filmId, @PathVariable("userId") long userId) throws ControllerException;

    @PostMapping("/note/eval/{filmId}/{userId}/{note}")
    ResponseEntity<String> evalFilm(@PathVariable("filmId") long filmId, @PathVariable("userId") long userId, @PathVariable("note") int note) throws ControllerException;



}
