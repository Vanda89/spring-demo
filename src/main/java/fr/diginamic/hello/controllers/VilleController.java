package fr.diginamic.hello.controllers;

import fr.diginamic.hello.entity.Ville;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/villes")
public class VilleController {

    /**
     * List of Ville objects to simulate a database
     */
    private final List<Ville> villes = new ArrayList<>();

    /**
     * Constructor to initialize the list with some sample data
     */
    public VilleController() {
        villes.add(new Ville(1, "Paris", 2148000));
        villes.add(new Ville(2, "Lyon", 515695));
        villes.add(new Ville(3, "Marseille", 861635));
        villes.add(new Ville(4, "Bordeaux", 257068));
    }

    /**
     * Get /villes
     *
     * @return the list of {@link Ville} objects
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ville non trouvée"));

    }

    /**
     * Post /villes/add -> Add a new city
     *
     * @param newVille the Ville object to add
     * @param result   BindingResult to capture validation errors
     * @return ResponseEntity with a success message or an error if the city already exists
     */
    @PostMapping("/add")
    public ResponseEntity<String> addVille(@Valid @RequestBody Ville newVille, BindingResult result) {

        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining("; "));
            return ResponseEntity.badRequest().body("Erreur de validation : " + errors);
        }

        boolean existingCity = villes.stream()
                .anyMatch(ville -> ville.getNom().equalsIgnoreCase(newVille.getNom()));

        if (existingCity) {
            return ResponseEntity.badRequest().body("La ville " + newVille.getNom() + " existe déjà.");
        }

        if(newVille.getId() == 0) {
            int maxId = villes.stream()
                    .mapToInt(Ville::getId)
                    .max()
                    .orElse(0);
            newVille.setId(maxId + 1);
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
    public ResponseEntity<String> updateVille(@PathVariable int id, @Valid @RequestBody Ville updated, BindingResult result) {

        if (id <= 0) {
            return ResponseEntity.badRequest().body("L'id doit être strictement positif");
        }

        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining("; "));

            return ResponseEntity.badRequest().body("Erreur de validation : " + errors);
        }

        Ville villeToUpdate = getVilleById(id);
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
    public ResponseEntity<String> deleteVille(@PathVariable int id) {
        Ville villeToDelete = getVilleById(id);

        villes.remove(villeToDelete);
        return ResponseEntity.ok("Ville supprimée avec succès : " + villeToDelete.getNom());
    }


}
