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
    public ResponseEntity<?> getAllVilles(@RequestParam int page, @RequestParam int size) {
        return villeService.extractVilles(page, size);
    }

    /**
     * Get /villes/{id} -> Get a city by its ID
     *
     * @param idVille the ID of the city to retrieve
     * @return ResponseEntity with the Ville object or an error if not found
     */
    @GetMapping(path = "/id/{idVille}")
    public ResponseEntity<?> getVilleById(@PathVariable int idVille) {
        return villeService.extractVilleById(idVille);
    }

    /**
     * Get /villes/search?ville={name} -> Get a city by its name
     *
     * @param ville the name of the city to retrieve
     * @return ResponseEntity with the Ville object or an error if not found
     */
    @GetMapping("/search")
    public ResponseEntity<?> getVilleByName(@RequestParam String ville) {
        return villeService.extractVilleByName(ville);
    }

    /**
     * Get /villes/search/start?prefix={prefix}
     *
     * @param prefix the starting of the city to retrieve
     * @return ResponseEntity with the Ville object or an error if not found
     *
     */
    @GetMapping("/search/start")
    public ResponseEntity<?> getCitiesStartingWith(@RequestParam String prefix) {
        return villeService.extractVilleStartsWith(prefix);
    }

    /**
     * Post /villes/add -> Add a new city
     * {
     *   "codeVille": "75056",
     *   "nom": "Paris",
     *   "nbHabitants": 2148000,
     *   "departement": {
     *     "code": "01"
     *   }
     * }
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
     * Get /villes/top/min?min={min} -> Get the most populated cities
     *
     * @param min  the minimum population
     * @return
     */
    @GetMapping("/top/min")
    public ResponseEntity<?> getTopVilles(@RequestParam int min) {
        return villeService.getTopNCitiesByPopulation(min);
    }

    /**
     * Get /villes/departement/population?min={min}&max={max} -> Get cities
     * with population within a specified range
     *
     * @param min             the minimum population
     * @param max             the maximum population
     * @return
     */

    @GetMapping("/departement/population")
    public ResponseEntity<?> getCitiesByRange(@RequestParam int min, @RequestParam int max) {
        return villeService.getCitiesByPopulationRange(min, max);
    }

    /**
     * Get /villes/top/departement/min?codeDepartement={codeDepartement}&min={min} -> Get th most populated cities of specific department
     *
     * @param codeDepartement the code of the department
     * @param min  the minimum population
     * @return
     */
    @GetMapping("/top/departement/min")
    public ResponseEntity<?> getTopVilleOfDepartements(@RequestParam String codeDepartement, @RequestParam int min) {
        return villeService.getTopNCitiesByPopulationOfDepartment(codeDepartement, min);
    }


    /**
     * Get /villes/population?codeDepartement={codeDepartement}&min={min}&max={max} -> Get cities of a department
     * with population within a specified range
     *
     * @param codeDepartement the code of the department
     * @param min             the minimum population
     * @param max             the maximum population
     * @return
     */
    @GetMapping("/population")
    public ResponseEntity<?> getCitiesByRangeOfDepartement(@RequestParam String codeDepartement,
                                              @RequestParam int min,
                                              @RequestParam int max) {
        return villeService.getCitiesByPopulationRangeOfDepartement(codeDepartement, min, max);
    }


    /**
     * Get /villes/top/departement/n?codeDepartement={codeDepartement}&n={n} -> Get the n most populated cities of a department
     *
     * @param codeDepartement the code of the department
     * @param n               the number of cities to retrieve
     * @return
     */
    @GetMapping("/top/departement/n")
    public ResponseEntity<?> getTopVillesofDepartementPaginated(@RequestParam String codeDepartement,
                                          @RequestParam int n) {
        return villeService.getTopNCitiesByPopulationOfDepartement(codeDepartement, n);
    }


}
