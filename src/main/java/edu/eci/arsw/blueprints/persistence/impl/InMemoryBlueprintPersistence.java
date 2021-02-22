/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.filter.filter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author hcadavid
 */
@Component("inmemory")
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {

    @Autowired
    private filter filter;

    private final Map<Tuple<String, String>, Blueprint> blueprints = new ConcurrentHashMap<>();

    public InMemoryBlueprintPersistence() {
        // load stub data
        Point[] pts = new Point[] { new Point(140, 140), new Point(115, 115) };
        Blueprint bp = new Blueprint("_authorname_", "_bpname_", pts);
        Blueprint bp2 = new Blueprint("Marx", "El Capital", pts);
        Blueprint bp3 = new Blueprint("Marx", "El manifiesto del partido comunista", pts);
        Blueprint bp4 = new Blueprint("El Rincon de Puh", "Winnie Pooh", pts);
        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);
        blueprints.put(new Tuple<>(bp4.getAuthor(), bp4.getName()), bp4);
    }

    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(), bp.getName()))) {
            throw new BlueprintPersistenceException("The given blueprint already exists: " + bp);
        } else {
            blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        try {
            return blueprints.get(new Tuple<>(author, bprintname));
        } catch (Exception e) {
            throw new BlueprintNotFoundException(e.toString());
        }
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> setBluePrints = new HashSet<Blueprint>();
        Set<Tuple<String, String>> setKeys = blueprints.keySet();
        for (Tuple<String, String> tuple : setKeys) {
            System.out.println(tuple.getElem1());
            if (tuple.getElem1().equals(author)) {
                setBluePrints.add(blueprints.get(tuple));
                System.out.println("Entre");
            }
        }
        return filter.filterBluePrints(setBluePrints);
    }

    @Override
    public Set<Blueprint> getAllBluePrints() throws BlueprintNotFoundException {
        Set<Blueprint> setBluePrints = new HashSet<Blueprint>();
        Set<Tuple<String, String>> setKeys = blueprints.keySet();
        for (Tuple<String, String> tuple : setKeys) {
            setBluePrints.add(blueprints.get(tuple));
        }
        return setBluePrints;
    }
}
