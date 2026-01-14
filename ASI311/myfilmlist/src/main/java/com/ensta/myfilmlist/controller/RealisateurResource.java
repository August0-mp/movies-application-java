package com.ensta.myfilmlist.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

import com.ensta.myfilmlist.dto.RealisateurDTO;
import com.ensta.myfilmlist.exception.ControllerException;
import com.ensta.myfilmlist.form.RealisateurForm;

import io.swagger.annotations.Api;
import io.swagger.models.Response;
import io.swagger.v3.oas.annotations.tags.Tag;

@Api(tags = "Realisateur")
@Tag(name = "Realisateur", description = "Opération sur les Realisateurs")
public interface RealisateurResource {

    @GetMapping
    ResponseEntity<List<RealisateurDTO>> getAllRealisateurs() throws ControllerException;
    
    @GetMapping("/{id}")
    ResponseEntity<RealisateurDTO> getRealisateurById(@Valid @PathVariable("id") long id) throws ControllerException;

    @PostMapping
    ResponseEntity<RealisateurDTO> createRealisateur(@Valid @RequestBody RealisateurForm realisateurForm) throws ControllerException;

}
