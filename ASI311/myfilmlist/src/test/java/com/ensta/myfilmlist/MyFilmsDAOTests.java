package com.ensta.myfilmlist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.ensta.myfilmlist.dao.FilmDAO;
import com.ensta.myfilmlist.model.Film;
import com.ensta.myfilmlist.model.Realisateur;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/data_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MyFilmsDAOTests {

    @Autowired
    private FilmDAO filmDao;

    @Test
    public void testFindById() {
        Optional<Film> filmOptional = filmDao.findById(1);
        assertTrue(filmOptional.isPresent());

        Film film = filmOptional.get();
        assertEquals("avatar", film.getTitre());
        assertEquals(162, film.getDuree());
    }

    @Test
    public void testFindByRealisateurId() {
        List<Film> films = filmDao.findByRealisateurId(2);
        assertEquals(3, films.size());
    }

    @Test
    public void testFindAll() {
        List<Film> films = filmDao.findAll();
        assertEquals(4, films.size());
    }
    
    @Test
    public void testSave() {
        Realisateur realisateur = new Realisateur();
        realisateur.setId(1);
        
        Film newFilm = new Film("Titanic", 195);
        newFilm.setRealisateur(realisateur);
        filmDao.save(newFilm);

        List<Film> films = filmDao.findAll();
        assertEquals(5, films.size());
    }
            
    /*
    @Test
    public void testFindNbFilmsByRealisateurId() {
        long count = filmDao.findNbFilmsByRealisateurId(2);
        assertEquals(3, count);
    }
    @Test
    public void testUpdate() {
        Film updatedFilm = new Film(1, "Avatar Remastered", 170, 1);
        filmDao.update(updatedFilm);
        Optional<Film> filmOptional = filmDao.findById(1);
        assertTrue(filmOptional.isPresent());

        Film film = filmOptional.get();
        assertEquals("Avatar Remastered", film.getTitre());
        assertEquals(170, film.getDuree());
    }
    */

}