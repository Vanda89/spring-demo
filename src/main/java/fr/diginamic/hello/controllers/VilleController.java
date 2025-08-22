package fr.diginamic.hello.controllers;

import fr.diginamic.hello.entity.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleController {

    private final VilleService villeService;

    /**
     * Constructor to inject the VilleService dependency.
     *
     * @param villeService
     */
    public VilleController(VilleService villeService) {
        this.villeService = villeService;
    }

    /**
     * Get /villes
     *
     * @return the list of {@link Ville} objects
     */
    @GetMapping
    public List<Ville> getVilles() {
        return villeService.extractVilles();
    }

    /**
     * Get /villes/{id} -> Get a city by its ID
     *
     * @param idVille the ID of the city to retrieve
     * @return ResponseEntity with the Ville object or an error if not found
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<Ville> getVilleById(@PathVariable Integer idVille) {
        Ville ville = villeService.extractVilleById(idVille);
        return ResponseEntity.ok(ville);

    }

    /**
     * Post /villes/add -> Add a new city
     *
     * @param newVille the Ville object to add
     * @return ResponseEntity with a success message or an error if the city already exists
     */
    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<List<Ville>> addVille(@RequestBody @Valid Ville newVille) {
        List<Ville> villes = villeService.insertVille(newVille);
        return ResponseEntity.ok(villes);
    }

    /**
     * Put /villes/update/{id} -> Update an existing city
     *
     * @param id the ID of the city to update
     * @param updatedVille the updated Ville object
     * @return ResponseEntity with a success message or an error if the city does not exist
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<List<Ville>> updateVille(@PathVariable Integer id, @RequestBody Ville updatedVille) {
        List<Ville> villes = villeService.updateVille(id, updatedVille);
        return ResponseEntity.ok(villes);
    }

    /**
     * Delete /villes/delete/{id} -> Delete a city by its ID
     *
     * @param id the ID of the city to delete
     * @return ResponseEntity with a success message or an error if the city does not exist
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteVille(@PathVariable int id) {
        villeService.deleteVille(id);
        return ResponseEntity.ok("Ville supprimée avec succès pour l'id : " + id);
    }

    /**
     * Get /villes/mostpopulated?codeDepartement={codeDepartement}&n={n} -> Get the n most populated cities of a department
     *
     * @param codeDepartement the code of the department
     * @param n the number of cities to retrieve
     * @return ResponseEntity with the list of Ville objects or an error if the department does not exist
     */
    @GetMapping("/mostpopulated")
    public ResponseEntity<List<Ville>> getMostPopulatedCities(String codeDepartement, int n) {

        List<Ville> mostPopulatedCities = villeService.getTopNVillesByPopulationOfDepartement(codeDepartement, n);
        return ResponseEntity.ok(mostPopulatedCities);

    }

    /**
     * Get /villes/rangedpopulated?codeDepartement={codeDepartement}&min={min}&max={max} -> Get cities of a department
     * with population within a specified range
     *
     * @param codeDepartement the code of the department
     * @param min the minimum population
     * @param max the maximum population
     * @return ResponseEntity with the list of Ville objects or an error if the department does not exist
     */
    @GetMapping("/rangedpopulated")
    public ResponseEntity<List<Ville>> getLargestCitiesOfDepartement(String codeDepartement, int min, int max) {

        List<Ville> largestCities = villeService.getCitiesByPopulationRangeOfDepartement(codeDepartement, min, max);
        return ResponseEntity.ok(largestCities);

    }


}
