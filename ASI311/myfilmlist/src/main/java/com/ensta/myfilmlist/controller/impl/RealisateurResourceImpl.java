package com.ensta.myfilmlist.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensta.myfilmlist.controller.RealisateurResource;
import com.ensta.myfilmlist.dto.RealisateurDTO;
import com.ensta.myfilmlist.exception.ControllerException;
import com.ensta.myfilmlist.form.RealisateurForm;
import com.ensta.myfilmlist.service.MyFilmsService;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;

import java.util.List;

@Validated
@RestController
@RequestMapping("/realisateur")
@CrossOrigin(origins = "http://localhost:3000") // Allow requests from your React app
public class RealisateurResourceImpl implements RealisateurResource {

    @Autowired
    private MyFilmsService myFilmsService;

    @ApiOperation(value = "Lister les Realisateurs", notes = "Permet de renvoyer la liste de tous les Realisateur.", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des Realisateurs a été renvoyée correctement")
    })
    @Override
    public ResponseEntity<List<RealisateurDTO>> getAllRealisateurs() throws ControllerException {
        try {
            List<RealisateurDTO> realisateurs = myFilmsService.findAllRealisateurs();
            return ResponseEntity.ok(realisateurs);
       
        } catch (Exception e) {
            throw new ControllerException("Error in searching for realisateurs.", e);
        }
    }

    
    @ApiOperation(value = "Liste un Realisateur par son ID", notes = "Permet de renvoyer un Realisateur par son ID.", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Le Realisateur a été renvoyé correctement"),
        @ApiResponse(code = 404, message = "Le Realisateur n'a pas été trouvé")
    })
    @Override
    public ResponseEntity<RealisateurDTO> getRealisateurById(long ID) throws ControllerException {
        try {
            RealisateurDTO realisateur = myFilmsService.findRealisateurById(ID);
            
            if(realisateur == null){
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(realisateur);
       
        } catch (Exception e) {
            throw new ControllerException("Error in searching for realisateurs.", e);
        }

    }

    @ApiOperation(value = "Cree un nouveau realisateur", notes = "Cree un nouveau realisateur en passant ses attributs par le body.", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Le realisateur a été crée correctement"),
    })
    @Override
    public ResponseEntity<RealisateurDTO> createRealisateur(@RequestBody RealisateurForm realisateurForm) throws ControllerException {
        try {
            RealisateurDTO createdRealisateur = myFilmsService.createRealisateur(realisateurForm);

            return ResponseEntity.status(201).body(createdRealisateur);

        } catch (Exception e) {
            throw new ControllerException("Error in creating the Realisateur.", e);
        }
    } 

}
