package com.ensta.myfilmlist.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensta.myfilmlist.controller.UtilisateurResource;
import com.ensta.myfilmlist.dto.FilmDTO;
import com.ensta.myfilmlist.exception.ControllerException;
import com.ensta.myfilmlist.form.FilmForm;
import com.ensta.myfilmlist.model.Utilisateur;
import com.ensta.myfilmlist.service.MyFilmsService;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

import java.util.List;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/utilisateur")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from your React app
public class UtilisateurResourceImpl implements UtilisateurResource {

    @Autowired
    private MyFilmsService myFilmsService;


    @ApiOperation(value = "Connexion d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Connexion réussie"),
            @ApiResponse(code = 401, message = "Email ou mot de passe incorrect"),
            @ApiResponse(code = 500, message = "Erreur interne du serveur")
    })
    @Override
    public ResponseEntity<Utilisateur> login(String email, String password) throws ControllerException {
        try {
            Utilisateur utilisateur = myFilmsService.login(email, password);
            return ResponseEntity.ok(utilisateur);

        } catch (Exception e) {
            throw new ControllerException("Erreur lors de la connexion.", e);
        }
    }

    @ApiOperation(value = "Inscription d'un nouvel utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Utilisateur créé avec succès"),
            @ApiResponse(code = 400, message = "Données invalides"),
            @ApiResponse(code = 500, message = "Erreur interne du serveur")
    })
    @Override
    public ResponseEntity<Utilisateur> signup(String email, String password, String prenom, String nom, boolean isAdmin) throws ControllerException {
        try {
            Utilisateur utilisateur = myFilmsService.signup(email, password, prenom, nom, isAdmin);
            return ResponseEntity.status(201).body(utilisateur);
        } catch (Exception e) {
            throw new ControllerException("Erreur lors de l'inscription.", e);
        }
    }




}
