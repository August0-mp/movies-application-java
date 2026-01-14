package com.ensta.myfilmlist;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.ensta.myfilmlist.dto.FilmDTO;
import com.ensta.myfilmlist.dto.RealisateurDTO;
import com.ensta.myfilmlist.form.FilmForm;
import com.ensta.myfilmlist.model.Film;
import com.ensta.myfilmlist.model.Realisateur;
import com.ensta.myfilmlist.service.MyFilmsService;
import com.ensta.myfilmlist.service.impl.MyFilmsServiceImpl;
import com.ensta.myfilmlist.exception.ServiceException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ensta.myfilmlist.dao.UtilisateurDAO;
import com.ensta.myfilmlist.dao.impl.JbdcUtilisateurDAO;
import com.ensta.myfilmlist.model.Utilisateur;

import java.util.Optional; // Utilisé pour le retour d'une valeur facultative de findByEmail

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



/**
 * Classe de tests du service MyFilmsServiceImpl.
 */
 @Component
public class MyfilmlistTests {

	/** Initialisation du service pour les traitements de l'application MyFilms */

	private MyFilmsService myFilmsService;

	public MyfilmlistTests(){

	}

	@Autowired	
	public MyfilmlistTests(MyFilmsService myFilmsService){
		this.myFilmsService = myFilmsService;
	}


	/**
	 * Permet de tester la mise a jour du statut "celebre" d'un RealisateurDTO en fonction du nombre de films realises.
	 */
	public void updateRealisateurCelebreTest() {
		// Creation des Realisateurs

		Realisateur jamesCameron = new Realisateur();
		jamesCameron.setNom("Cameron");
		jamesCameron.setPrenom("James");
		jamesCameron.setDateNaissance(LocalDate.of(1954, 8, 16));

		Realisateur peterJackson = new Realisateur();
		peterJackson.setNom("Jackson");
		peterJackson.setPrenom("Peter");
		peterJackson.setDateNaissance(LocalDate.of(1961, 10, 31));

		// Creation des films

		Film avatar = new Film();
		avatar.setTitre("Avatar");
		avatar.setDuree(162);
		avatar.setRealisateur(jamesCameron);

		Film laCommunauteDeLAnneau = new Film();
		laCommunauteDeLAnneau.setTitre("La communauté de l'anneau");
		laCommunauteDeLAnneau.setDuree(178);
		laCommunauteDeLAnneau.setRealisateur(peterJackson);

		Film lesDeuxTours = new Film();
		lesDeuxTours.setTitre("Les deux tours");
		lesDeuxTours.setDuree(179);
		lesDeuxTours.setRealisateur(peterJackson);

		Film leRetourDuRoi = new Film();
		leRetourDuRoi.setTitre("Le retour du roi");
		leRetourDuRoi.setDuree(201);
		leRetourDuRoi.setRealisateur(peterJackson);

		// Affectation des films aux realisateurs

		List<Film> peterJacksonFilms = new ArrayList<>();
		peterJacksonFilms.add(laCommunauteDeLAnneau);
		peterJacksonFilms.add(lesDeuxTours);
		peterJacksonFilms.add(leRetourDuRoi);
		peterJackson.setFilmRealises(peterJacksonFilms);

		List<Film> jamesCameronFilms = new ArrayList<>();
		jamesCameronFilms.add(avatar);
		jamesCameron.setFilmRealises(jamesCameronFilms);

		// Mise a jour du statut "celebre" des Realisateurs

		try {
			jamesCameron = myFilmsService.updateRealisateurCelebre(jamesCameron);
			peterJackson = myFilmsService.updateRealisateurCelebre(peterJackson);

			// Attendue : false
			System.out.println("James Cameron est-il celebre ? " + jamesCameron.isCelebre());
			// Attendue : true
		System.out.println("Peter Jackson est-il celebre ? " + peterJackson.isCelebre());
		} catch (ServiceException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
	}
	}

	/**
	 * Permet de tester le calcul de la duree totale des films.
	 */
	public void calculerDureeTotaleTest() {
		// Creation des films
		Film laCommunauteDeLAnneau = new Film();
		laCommunauteDeLAnneau.setTitre("La communauté de l'anneau");
		laCommunauteDeLAnneau.setDuree(178);

		Film lesDeuxTours = new Film();
		lesDeuxTours.setTitre("Les deux tours");
		lesDeuxTours.setDuree(179);

		Film leRetourDuRoi = new Film();
		leRetourDuRoi.setTitre("Le retour du roi");
		leRetourDuRoi.setDuree(201);

		List<Film> leSeigneurDesAnneaux = new ArrayList<>();
		leSeigneurDesAnneaux.add(laCommunauteDeLAnneau);
		leSeigneurDesAnneaux.add(lesDeuxTours);
		leSeigneurDesAnneaux.add(leRetourDuRoi);

		// Calcule de la duree totale

		long dureeTotale = myFilmsService.calculerDureeTotale(leSeigneurDesAnneaux);
		// Attendue : 558 minutes
		System.out.println("La duree totale de la trilogie \"Le Seigneur des Anneaux\" est de : " + dureeTotale + " minutes");
	}

	/**
	 * Permet de tester le calcul de la note moyenne des films.
	 */
	public void calculerNoteMoyenneTest() {
		// Creation des notes

		double[] notes = { 18, 15.5, 12 };

		// Calcul de la note moyenne

		double noteMoyenne = myFilmsService.calculerNoteMoyenne(notes);

		// Attendue : 15,17
		System.out.println("La note moyenne est : " + noteMoyenne);
	}


	//Permet de tester la nouvelle version updateRealisateurCelebres() BONUS
	// Test de la méthode updateRealisateurCelebres
	public void testUpdateRealisateurCelebres() {
		// Création des films
		List<Film> films = createFilms();

		// Création des réalisateurs avec les films
		List<Realisateur> realisateurs = new ArrayList<>();
		realisateurs.add(createRealisateur("Realisateur 1", films.subList(0, 2))); // 2 films
		realisateurs.add(createRealisateur("Realisateur 2", films.subList(2, 4))); // 2 films
		realisateurs.add(createRealisateur("Realisateur 3", films)); // 4 films

		// Bloc try-catch pour capturer les exceptions
		try {
			// Appeler la méthode à tester
			List<Realisateur> realisateursCelebres = MyFilmsServiceImpl.updateRealisateurCelebres(realisateurs);

			// Vérification du résultat
			System.out.println("Test de la méthode updateRealisateurCelebres :");
			if (realisateursCelebres.size() == 1) {
				System.out.println("Test réussi : 1 réalisateur célèbre trouvé.");
			} else {
				System.out.println("Test échoué : Nombre de réalisateurs célèbres incorrect.");
			}

			// Vérification du réalisateur célèbre
			Realisateur celebre = realisateursCelebres.get(0);
			if (celebre.isCelebre() && "Realisateur 3".equals(celebre.getNom())) {
				System.out.println("Test réussi : Le réalisateur célèbre est correct.");
			} else {
				System.out.println("Test échoué : Le réalisateur célèbre est incorrect.");
			}
		} catch (ServiceException e) {
			// Capturer et afficher l'exception en cas d'erreur
			System.out.println("Une erreur est survenue : " + e.getMessage());
		}
	}

	// Méthode pour créer une liste de films
	private List<Film> createFilms() {
		List<Film> films = new ArrayList<>();
		films.add(new Film("Film 1", 120));
		films.add(new Film("Film 2", 90));
		films.add(new Film("Film 3", 100));
		films.add(new Film("Film 4", 110));
		return films;
	}

	// Méthode pour créer un réalisateur avec des films spécifiques
	private Realisateur createRealisateur(String nom, List<Film> films) {
		return new Realisateur(nom, films);
	}

	/**
	 * Permet de tester la recuperation des films.
	 */
	public void findAllFilmsTest() {
		try {
			List<FilmDTO> films = myFilmsService.findAllFilms();

			// Attendue : 4
			System.out.println("Combien y a-t-il de films ? " + films.size());

			films.forEach(System.out::println);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de tester la creation des films.
	 */
	public void createFilmTest() {
		try {
			RealisateurDTO realisateurDTO = myFilmsService.findRealisateurByNomAndPrenom("Cameron", "James");

			FilmForm titanic = new FilmForm();
			titanic.setTitre("Titanic");
			titanic.setDuree(195);
			titanic.setRealisateurId(realisateurDTO.getId());

			FilmDTO newFilm = myFilmsService.createFilm(titanic);

			System.out.println("Le nouveau film 'Titanic' possede l'id : " + newFilm.getId());

			List<FilmDTO> films = myFilmsService.findAllFilms();

			// Attendue : 5
			System.out.println("Combien y a-t-il de films ? " + films.size());

			films.forEach(f -> System.out.println("Le realisateur du film : '" + f.getTitre() + "' est : " + f.getRealisateur()));
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de tester la recuperation d'un film par son identifiant.
	 */
	public void findFilmByIdTest() {
		try {
			FilmDTO avatar = myFilmsService.findFilmById(2);
			System.out.println("Le film avec l'identifiant 2 est : " + avatar);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de tester la recuperation d'un film par son identifiant.
	 */
	public void addFilmToFavoriteTest() {
		try {
			myFilmsService.addFilmToFavorite(1, 1);
			myFilmsService.addFilmToFavorite(2, 1);
			myFilmsService.addFilmToFavorite(3, 1);
			myFilmsService.addFilmToFavorite(4, 1);
			List<FilmDTO> favFilms = myFilmsService.findUserFavoriteFilms(1);
			favFilms.forEach(System.out::println);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de tester la recuperation d'un film par son identifiant.
	 */
	public void removeFilmToFavoriteTest() {
		try {
			myFilmsService.removeFilmFromFavorite(2, 1);
			myFilmsService.removeFilmFromFavorite(3, 1);
			List<FilmDTO> favFilms = myFilmsService.findUserFavoriteFilms(1);
			favFilms.forEach(System.out::println);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de tester la recuperation d'un film par son identifiant.
	 */
	public void evalFilmTest() {
		try {
			myFilmsService.evalFilm(1, 2, 16);
			myFilmsService.evalFilm(2, 2, 18);
			myFilmsService.evalFilm(4, 2, 18);

			myFilmsService.evalFilm(1, 1, 10);
			myFilmsService.evalFilm(4, 1, 15);
			Double film1AvgNote = myFilmsService.findFilmAverageNote(1);
			Double film2AvgNote = myFilmsService.findFilmAverageNote(2);
			Double film3AvgNote = myFilmsService.findFilmAverageNote(3);
			Double film4AvgNote = myFilmsService.findFilmAverageNote(4);

			Integer film1PerNote = myFilmsService.findFilmPersonalNote(1, 1);
			Integer film2PerNote = myFilmsService.findFilmPersonalNote(2, 1);
			Integer film3PerNote = myFilmsService.findFilmPersonalNote(3, 1);
			Integer film4PerNote = myFilmsService.findFilmPersonalNote(4, 1);

			System.out.println("Film1 Avg Note: " + film1AvgNote);
			System.out.println("Film2 Avg Note: " + film2AvgNote);
			System.out.println("Film3 Avg Note: " + film3AvgNote);
			System.out.println("Film4 Avg Note: " + film4AvgNote);

			System.out.println("Film1 Personal Note: " + film1PerNote);
			System.out.println("Film2 Personal Note: " + film2PerNote);
			System.out.println("Film3 Personal Note: " + film3PerNote);
			System.out.println("Film4 Personal Note: " + film4PerNote);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public void loginTest() {
		try {
			// Good login credentials
			Utilisateur utilisateur = myFilmsService.login("salima@salima.com", "salima");
			System.out.println("Login success: " + utilisateur.getId());
			// Bad password
//			myFilmsService.login("salima@salima.com", "salima1");
			// Bad email
//			myFilmsService.login("salima1@salima.com", "salima1");
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}

	public void signupTest() {
		try {
			// Good login credentials
			Utilisateur utilisateur = myFilmsService.signup("djo@djo.com", "djo", "djo", "djo", true);
			System.out.println("Signup success: " + utilisateur.getId());
			// Bad password
//			myFilmsService.login("salima@salima.com", "salima1");
			// Bad email
//			myFilmsService.login("salima1@salima.com", "salima1");
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Permet de tester la suppression d'un film avec son identifiant.
	 */
	public void deleteFilmByIdTest() {
	try {
			FilmDTO filmDTO = myFilmsService.findFilmById(5);
			System.out.println("Le film avec l'identifiant 5 est : " + filmDTO);
			myFilmsService.deleteFilm(5);
			filmDTO = myFilmsService.findFilmById(5);

			System.out.println("Suppression du film avec l'identifiant 5...");
			System.out.println("Le film avec l'identifiant 5 est : " + filmDTO);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de tester la mise a jour du statut celebre d'un Realisateur.
	 */
	public void updateRealisateurCelebre() {
		try {
			RealisateurDTO realisateurDTO = myFilmsService.findRealisateurByNomAndPrenom("Cameron", "James");
			// Attendue : false
			System.out.println("James Cameron est-il celebre ? " + realisateurDTO.isCelebre());

			FilmForm titanic = new FilmForm();
			titanic.setTitre("Titanic");
			titanic.setDuree(195);
			titanic.setRealisateurId(realisateurDTO.getId());

			FilmForm leHobbit = new FilmForm();
			leHobbit.setTitre("Le Hobbit : Un voyage inattendu");
			leHobbit.setDuree(169);
			leHobbit.setRealisateurId(realisateurDTO.getId());

			myFilmsService.createFilm(titanic);
			FilmDTO leHobbitDTO = myFilmsService.createFilm(leHobbit);

			System.out.println("James Cameron a realise deux nouveaux films");
			realisateurDTO = myFilmsService.findRealisateurByNomAndPrenom("Cameron", "James");

			// Attendue : true
			System.out.println("James Cameron est-il celebre ? " + realisateurDTO.isCelebre());

			myFilmsService.deleteFilm(leHobbitDTO.getId());
			System.out.println("Ce n'est pas James Cameron qui a realise le Hobbit, suppression du film !");
			realisateurDTO = myFilmsService.findRealisateurByNomAndPrenom("Cameron", "James");

			// Attendue : false
			System.out.println("James Cameron est-il celebre ? " + realisateurDTO.isCelebre());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}
