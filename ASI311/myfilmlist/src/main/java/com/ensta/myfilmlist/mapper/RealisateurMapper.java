package com.ensta.myfilmlist.mapper;

import com.ensta.myfilmlist.dto.RealisateurDTO;
import com.ensta.myfilmlist.form.FilmForm;
import com.ensta.myfilmlist.form.RealisateurForm;
import com.ensta.myfilmlist.model.Film;
import com.ensta.myfilmlist.model.Realisateur;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Effectue les conversions des Realisateurs entre les couches de l'application.
 */
public class RealisateurMapper {

	/**
	 * Convertit une liste de realisateurs en liste de DTO.
	 * 
	 * @param realisateurs la liste des realisateurs
	 * @return Une liste non nulle de dtos construite a partir de la liste des realisateurs.
	 */
	public static List<RealisateurDTO> convertRealisateurToRealisateurDTOs(List<Realisateur> realisateurs) {
		return realisateurs.stream()
				.map(RealisateurMapper::convertRealisateurToRealisateurDTO)
				.collect(Collectors.toList());
	}

	/**
	 * Convertit un realisateur en DTO.
	 * 
	 * @param realisateur le realisateeur a convertir
	 * @return Un DTO construit a partir des donnees du realisateur.
	 */
	public static RealisateurDTO convertRealisateurToRealisateurDTO(Realisateur realisateur) {
		if (realisateur == null)
			return null;

		RealisateurDTO realisateurDTO = new RealisateurDTO();
		realisateurDTO.setId(realisateur.getId());
		realisateurDTO.setPrenom(realisateur.getPrenom());
		realisateurDTO.setNom(realisateur.getNom());
		realisateurDTO.setDateNaissance(realisateur.getDateNaissance());
		realisateurDTO.setCelebre(realisateur.isCelebre());

		return realisateurDTO;
	}

	/**
	 * Convertit un DTO en realisateur.
	 * 
	 * @param realisateurDTO le DTO a convertir
	 * @return Un Realisateur construit a partir des donnes du DTO.
	 */
	public static Realisateur convertRealisateurDTOToRealisateur(RealisateurDTO realisateurDTO) {
		if (realisateurDTO == null)
			return null;

		Realisateur realisateur = new Realisateur();
		realisateur.setId(realisateurDTO.getId());
		realisateur.setPrenom(realisateurDTO.getPrenom());
		realisateur.setNom(realisateurDTO.getNom());
		realisateur.setDateNaissance(realisateurDTO.getDateNaissance());
		realisateur.setCelebre(realisateurDTO.isCelebre());

		return realisateur;
	}

	/**
	 * Convertit un Form en film.
	 *
	 * @param filmForm le Form a convertir
	 * @return Un Film construit a partir des donnes du Form.
	 */
	public static Realisateur convertRealisateurFormToRealisateur(RealisateurForm realisateurForm) {
		Realisateur realisateur = new Realisateur();
		realisateur.setPrenom(realisateurForm.getPrenom());
		realisateur.setNom(realisateurForm.getNom());
		realisateur.setDateNaissance(realisateurForm.getDateNaissance());
		realisateur.setCelebre(false);

		return realisateur;
	}
}
