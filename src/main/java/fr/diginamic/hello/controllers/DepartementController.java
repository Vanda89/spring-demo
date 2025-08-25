package fr.diginamic.hello.controllers;

import fr.diginamic.hello.entity.Departement;
import fr.diginamic.hello.services.DepartementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/departements")
public class DepartementController {

    // The DepartementService is injected here to handle business logic
    private final DepartementService departementService;

    /**
     * Constructor to inject the DepartementService dependency.
     *
     * @param departementService the DepartementService instance to be used by this controller
     */
    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    /**
     * Get /departements
     *
     * @return the list of {@link Departement} objects
     */
    @GetMapping
    public List<Departement> getDepartements() {
        return departementService.extractDepartements();
    }

    /**
     * Get /departements/{id} -> Get a department by its ID
     *
     * @param id the ID of the department to retrieve
     * @return ResponseEntity with the Departement object or an error if not found
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<Departement> getDepartementById(@PathVariable Integer id) {
        Departement departement = departementService.extractDepartementById(id);
        return ResponseEntity.ok(departement);
    }

    /**
     * Post /departements/add -> Add a new department
     *
     * @param departement the Departement object to add
     * @return ResponseEntity with the updated list of departments
     */
    @PostMapping(path = "/add")
    public ResponseEntity<List<Departement>> addDepartement(@Valid @RequestBody Departement departement) {
        List<Departement> updatedDepartements = departementService.insertDepartement(departement);
        return ResponseEntity.ok(updatedDepartements);
    }

    /**
     * Put /departements/update/{id} -> Update an existing department
     *
     * @param idDepartement     the ID of the department to update
     * @param updatedDepartement the updated Departement object
     * @return ResponseEntity with the updated list of departments
     */
    @PutMapping(path = "/update/{id}")
    public ResponseEntity<List<Departement>> updateDepartement(@PathVariable Integer idDepartement,@Valid @RequestBody Departement updatedDepartement) {
        List<Departement> departements = departementService.updateDepartement(idDepartement, updatedDepartement);
        return ResponseEntity.ok(departements);
    }

    /**
     * Delete /departements/delete/{id} -> Delete a department by its ID
     *
     * @param idDepartement the ID of the department to delete
     * @return ResponseEntity with a success message
     */
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteDepartement(@PathVariable Integer idDepartement) {
        departementService.deleteDepartement(idDepartement);
        return ResponseEntity.ok("Departement supprimé avec succès pour l'id : " + idDepartement);
    }


}
