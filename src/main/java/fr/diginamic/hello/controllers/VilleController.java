package fr.diginamic.hello.controllers;

import fr.diginamic.hello.entity.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<?> getAllVilles() {
        return villeService.extractVilles();
    }

    /**
     * Get /villes/{id} -> Get a city by its ID
     *
     * @param idVille the ID of the city to retrieve
     * @return ResponseEntity with the Ville object or an error if not found
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getVilleById(@PathVariable int idVille) {
        return villeService.extractVilleById(idVille);
    }

    /**
     * Get /villes/name?ville={name} -> Get a city by its name
     *
     * @param ville the name of the city to retrieve
     * @return ResponseEntity with the Ville object or an error if not found
     */
    @GetMapping("/search")
    public ResponseEntity<?> getVilleByName(@RequestParam String ville) {
        return villeService.extractVilleByName(ville);
    }

    /**
     * Post /villes/add -> Add a new city
     *
     * @param ville  the Ville object to add
     * @param result the BindingResult containing validation results
     * @return ResponseEntity with the created Ville object or validation errors
     */
    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<?> insertVille(@Valid @RequestBody Ville ville, BindingResult result) {
        return villeService.insertVille(ville, result);
    }

    /**
     * Put /villes/update/{id} -> Update an existing city
     *
     * @param idVille      the ID of the city to update
     * @param villeUpdated the updated Ville object
     * @return ResponseEntity with a success message or an error if the city does not exist
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVille(@PathVariable int idVille,
                                         @Valid @RequestBody Ville villeUpdated,
                                         BindingResult result) {
        return villeService.updateVille(idVille, villeUpdated, result);
    }

    /**
     * Delete /villes/delete/{id} -> Delete a city by its ID
     *
     * @param idVille the ID of the city to delete
     * @return ResponseEntity with a success message or an error if the city does not exist
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVille(@PathVariable int idVille) {
        return villeService.deleteVille(idVille);
    }

    /**
     * Get /villes/mostpopulated?codeDepartement={codeDepartement}&n={n} -> Get the n most populated cities of a department
     *
     * @param codeDepartement the code of the department
     * @param n               the number of cities to retrieve
     * @return ResponseEntity with the list of Ville objects or an error if the department does not exist
     */
    @GetMapping("/mostpopulated")
    public ResponseEntity<?> getTopVilles(@RequestParam String codeDepartement,
                                          @RequestParam int n) {
        return villeService.getTopNVillesByPopulationOfDepartement(codeDepartement, n);
    }

    /**
     * Get /villes/rangedpopulated?codeDepartement={codeDepartement}&min={min}&max={max} -> Get cities of a department
     * with population within a specified range
     *
     * @param codeDepartement the code of the department
     * @param min             the minimum population
     * @param max             the maximum population
     * @return ResponseEntity with the list of Ville objects or an error if the department does not exist
     */
    @GetMapping("/rangedpopulated")
    public ResponseEntity<?> getCitiesByRange(@RequestParam String codeDepartement,
                                              @RequestParam int min,
                                              @RequestParam int max) {
        return villeService.getCitiesByPopulationRangeOfDepartement(codeDepartement, min, max);
    }


}
