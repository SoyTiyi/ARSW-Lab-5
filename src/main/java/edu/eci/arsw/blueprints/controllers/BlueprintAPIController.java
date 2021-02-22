/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
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

    @RequestMapping(value = "/{author}", method = RequestMethod.GET)
    public ResponseEntity<?> handleBluePrintsGetByAuthor(@PathVariable String author){
        try {
            System.out.println("Entre autor "+author);
            System.out.println(bs.getBlueprintsByAuthor(author).size());
            return new ResponseEntity<>(bs.getBlueprintsByAuthor(author),HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{author}/{bpname}", method = RequestMethod.GET)
    public ResponseEntity<?> handleBluePrintsGetByAuthorAndBlueprint(@PathVariable String author, @PathVariable String bpname){
        try {
            return new ResponseEntity<>(bs.getBlueprint(author, bpname),HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addNewBlueprint(@RequestBody JSONObject bp){
        try {
            System.out.println(bp.get("author").toString());
            System.out.println(bp.get("name").toString());
            Blueprint newBluePrint = new Blueprint(bp.get("author").toString(), bp.get("name").toString());
            String[] points = bp.get("points").toString().split("-");
            System.out.println(bp.get("points").toString());
            for(String point: points){
                List<String> cors = Arrays.asList(point.split(","));
                Point newPoint = new Point(Integer.parseInt(cors.get(0).substring(2)),Integer.parseInt(cors.get(1).substring(2,cors.get(0).length())));
                System.out.println(newPoint.getX()+" x de "+point);
                System.out.println(newPoint.getY()+" y de "+point);
                newBluePrint.addPoint(newPoint);
            }
            bs.addNewBlueprint(newBluePrint);
            return new ResponseEntity<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN.getReasonPhrase(), HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/{author}/{bpname}", method = RequestMethod.PUT)
    public ResponseEntity<?> setBluePrint(@PathVariable String author, @PathVariable String bpname,@RequestBody JSONObject bp){
        try {
            Blueprint oldBluePrint = bs.getBlueprint(author, bpname);
            oldBluePrint.setAuthor(bp.get("author").toString());
            oldBluePrint.setName(bp.get("name").toString());
            String[] points = bp.get("points").toString().split("-");
            System.out.println(bp.get("points").toString());
            List<Point> nullPoints= Arrays.asList();
            for(String point: points){
                List<String> cors = Arrays.asList(point.split(","));
                Point newPoint = new Point(Integer.parseInt(cors.get(0).substring(2)),Integer.parseInt(cors.get(1).substring(2,cors.get(0).length())));
                System.out.println(newPoint.getX()+" x de "+point);
                System.out.println(newPoint.getY()+" y de "+point);
                nullPoints.add(newPoint);
            }
            oldBluePrint.setPoints(nullPoints);
            Set<Blueprint> set = new HashSet<Blueprint>();
            set.add(oldBluePrint);
            return new ResponseEntity<>(set, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST);
        }
    }
}

