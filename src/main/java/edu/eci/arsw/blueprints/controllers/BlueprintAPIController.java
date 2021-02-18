/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.services.BlueprintsServices;

/**
 *
 * @author hcadavid
 */


@RestController
@RequestMapping(value="/blueprints")

public class BlueprintAPIController {
    
    @Autowired
    private BlueprintsServices bs;


    @RequestMapping(method = RequestMethod.GET)

    public ResponseEntity<?> handleBluePrintsGet(){
        try {
            return new ResponseEntity<>(bs.getAllBlueprints(),HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.NOT_FOUND);
        }
    }
    
    
    
}

