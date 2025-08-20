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
        villes.add(new Ville(1, "Paris", 2148000));
        villes.add(new Ville(2, "Lyon", 515695));
        villes.add(new Ville(3, "Marseille", 861635));
        villes.add(new Ville(4, "Bordeaux", 257068));
    }

    /**
     * Get /villes
     *
     * @return the list of Ville objects
     */
    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }

    /**
     * Get /villes/{id} -> Get a city by its ID
     *
     * @param id the ID of the city to retrieve
     * @return the Ville object with the specified ID, or null if not found
     */
    @GetMapping(path = "/{id}")
    public Ville getVilleById(@PathVariable Integer id) {
        return villes.stream()
                .filter(ville -> ville.getId() == id)
                .findFirst()
                .orElse(null);

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

    /**
     * Put /villes/update/{id} -> Update an existing city
     *
     * @param id      the ID of the city to update
     * @param updated the updated Ville object
     * @return ResponseEntity with a success message or an error if the city does not exist
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateVille(@PathVariable Integer id, @RequestBody Ville updated) {
        Ville villeToUpdate = getVilleById(id);
        if (villeToUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        villeToUpdate.setNom(updated.getNom());
        villeToUpdate.setNbHabitants(updated.getNbHabitants());

        return ResponseEntity.ok("Ville mise à jour avec succès : " + updated.getNom());
    }

    /**
     * Delete /villes/delete/{id} -> Delete a city by its ID
     *
     * @param id the ID of the city to delete
     * @return ResponseEntity with a success message or an error if the city does not exist
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVille(@PathVariable Integer id) {
        Ville villeToDelete = getVilleById(id);
        if (villeToDelete == null) {
            return ResponseEntity.notFound().build();
        }

        villes.remove(villeToDelete);
        return ResponseEntity.ok("Ville supprimée avec succès : " + villeToDelete.getNom
                ());
    }


}
