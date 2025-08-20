package fr.diginamic.hello.controllers;

import fr.diginamic.hello.entity.Ville;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleController {

    private final List<Ville> villes = new ArrayList<>();

    public VilleController() {
        villes.add(new Ville("Paris", 2148000));
        villes.add(new Ville("Lyon", 515695));
        villes.add(new Ville("Marseille", 861635));
        villes.add(new Ville("Bordeaux", 257068));
    }

    /** Get /villes
     *
     * @return the list of Ville objects
     */
    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }

    /**
     * Post /villes/add -> Add a new city to the list
     *
     * @param newVille the new city to add
     * @return ResponseEntity with a success message or an error if the city already exists
     */
    @PostMapping("/add")
    public ResponseEntity<String> addVille(@RequestBody Ville newVille) {
        boolean existingCity = villes.stream()
                .anyMatch(v -> v.getNom().equalsIgnoreCase(newVille.getNom()));

        if (existingCity) {
            return ResponseEntity.badRequest().body("La ville " + newVille.getNom() + " existe déjà.");
        }

        villes.add(newVille);
        return ResponseEntity.ok("Ville insérée avec succès : " + newVille.getNom());
    }


}
