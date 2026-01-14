package com.ensta.myfilmlist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ensta.myfilmlist.exception.ControllerException;
import com.ensta.myfilmlist.model.Utilisateur;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;

@Api(tags = "Utilisateur")
@Tag(name = "Utilisateur", description = "Opération sur les utilisateurs")
public interface UtilisateurResource {
    
    @PostMapping("/login")
    ResponseEntity<Utilisateur> login(@RequestParam("email") String email, @RequestParam("password") String password) throws ControllerException;

    @PostMapping("/signup")
    ResponseEntity<Utilisateur> signup(
        @RequestParam("email") String email,
        @RequestParam("password") String password,
        @RequestParam("prenom") String prenom,
        @RequestParam("nom") String nom,
        @RequestParam("isAdmin") boolean isAdmin
    ) throws ControllerException;


}
