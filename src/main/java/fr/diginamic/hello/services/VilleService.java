package fr.diginamic.hello.services;

import fr.diginamic.hello.daos.DepartementDao;
import fr.diginamic.hello.daos.VilleDao;
import fr.diginamic.hello.dtos.VilleDto;
import fr.diginamic.hello.dtos.VilleMapper;
import fr.diginamic.hello.entity.Departement;
import fr.diginamic.hello.entity.Ville;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VilleService {

    // The VilleDao is injected here to interact with the database
    private final VilleDao villeDao;
    private final DepartementDao departementDao;


    /**
     * Constructor to inject the VilleDao dependency.
     *
     * @param villeDao the VilleDao instance to be used by this service
     */
    public VilleService(VilleDao villeDao, DepartementDao departementDao) {
        this.villeDao = villeDao;
        this.departementDao = departementDao;
    }

    /**
     * Retrieves all Ville entities from the database.
     *
     * @return a ResponseEntity containing a list of all Ville entities
     */
    @Transactional
    public ResponseEntity<?> extractVilles() {
        List<Ville> villes = villeDao.getAllVilles();

        if (villes == null || villes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Aucune ville trouvée");
        }

        List<VilleDto> villesDto = villes.stream()
                .map(VilleMapper::toDto)
                .toList();


        return ResponseEntity.ok(villesDto);
    }

    /**
     * Retrieves a Ville entity by its ID.
     *
     * @param id the ID of the Ville to retrieve
     * @return the Ville entity with the specified ID
     * @throws IllegalArgumentException if the ID is less than or equal to 0
     * @throws EntityNotFoundException  if no Ville with the given ID exists
     */
    @Transactional
    public ResponseEntity<?> extractVilleById(int id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().body("L'id de la ville doit être supérieur à 0");
        }

        Ville ville = villeDao.getVilleById(id);
        if (ville == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ville avec l'id " + id + " non trouvée");
        }

        VilleDto villeDto = VilleMapper.toDto(ville);
        return ResponseEntity.ok(villeDto);
    }

    /**
     * Retrieves a Ville entity by its name.
     *
     * @param nom the name of the Ville to search for
     * @return the Ville entity with the specified name
     * @throws IllegalArgumentException if the name is empty
     * @throws EntityNotFoundException  if no Ville with the given name exists
     */
    @Transactional
    public ResponseEntity<?> extractVilleByName(String nom) {
        if (nom == null || nom.isEmpty()) {
            return ResponseEntity.badRequest().body("Le nom de la ville ne peut pas être vide");
        }
        Ville ville = villeDao.getVilleByName(nom);
        if (ville == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucune ville trouvée avec le nom " + nom);
        }

        VilleDto villeDto = VilleMapper.toDto(ville);
        return ResponseEntity.ok(villeDto);
    }

    /**
     * Inserts a new Ville entity into the database.
     *
     * @param ville the Ville object to insert
     * @return a list of all Ville entities after the insertion
     * @throws IllegalArgumentException if the input data is invalid
     */
    @Transactional
    public ResponseEntity<?> insertVille(Ville ville, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if (ville == null) {
            return ResponseEntity.badRequest().body("La ville ne peut pas être null");
        }
        if (ville.getNom() == null || ville.getNom().length() < 2) {
            return ResponseEntity.badRequest().body("Le nom de la ville doit contenir au moins 2 caractères");
        }
        if (ville.getNbHabitants() < 1) {
            return ResponseEntity.badRequest().body("Le nombre d'habitants doit être supérieur à 0");
        }

        Departement dep = departementDao.getDepartementByCode(ville.getDepartement().getCode());
        if (dep == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Département avec le code " + ville.getDepartement().getCode() + " non trouvé");
        }

        ville.setDepartement(dep);
        villeDao.insertVille(ville);

        List<Ville> villes = villeDao.getAllVilles();
        List<VilleDto> villesDto = villes.stream()
                .map(VilleMapper::toDto)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(villesDto);
    }

    /**
     * Updates an existing Ville entity.
     *
     * @param idVille      the ID of the Ville to update
     * @param villeUpdated the updated Ville object
     * @return a list of all Ville entities after the update
     * @throws IllegalArgumentException if no Ville with the given ID exists or if input data is invalid
     * @throws EntityNotFoundException  if no Ville with the given ID exists
     */
    @Transactional
    public ResponseEntity<?> updateVille(int idVille, Ville villeUpdated, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        if (idVille <= 0) {
            return ResponseEntity.badRequest().body("L'id doit être strictement positif");
        }
        if (villeUpdated.getNom() == null || villeUpdated.getNom().trim().length() < 2) {
            return ResponseEntity.badRequest().body("Le nom de la ville doit contenir au moins 2 caractères");
        }
        if (villeUpdated.getNbHabitants() < 1) {
            return ResponseEntity.badRequest().body("Le nombre d'habitants doit être supérieur à 0");
        }
        if (villeUpdated.getDepartement() == null || villeUpdated.getDepartement().getCode() == null) {
            return ResponseEntity.badRequest().body("Le département doit être renseigné");
        }

        Departement dep = departementDao.getDepartementByCode(villeUpdated.getDepartement().getCode());
        if (dep == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Département non trouvé pour code " + villeUpdated.getDepartement().getCode());
        }

        villeUpdated.setDepartement(dep);
        villeDao.updateVille(idVille, villeUpdated);

        List<Ville> villes = villeDao.getAllVilles();
        List<VilleDto> villesDto = villes.stream()
                .map(VilleMapper::toDto)
                .toList();

        return ResponseEntity.ok(villesDto);
    }

    /**
     * Deletes a Ville entity by its ID.
     *
     * @param idVille the ID of the Ville to delete
     * @throws IllegalArgumentException if no Ville with the given ID exists
     * @throws EntityNotFoundException  if no Ville with the given ID exists
     */
    @Transactional
    public ResponseEntity<?> deleteVille(int idVille) {
        if (idVille <= 0) {
            return ResponseEntity.badRequest().body("L'id doit être strictement positif");
        }
        Ville ville = villeDao.getVilleById(idVille);
        if (ville == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ville avec l'id " + idVille + " non trouvée");
        }
        villeDao.deleteVille(idVille);
        return ResponseEntity.ok("Ville supprimée avec succès avec l'id : " + idVille);
    }

    /**
     * Retrieves the top N Ville entities by population for a given department code.
     *
     * @param codeDepartement the code of the department
     * @param n               the number of top cities to retrieve
     * @return a list of the top N Ville entities by population
     * @throws IllegalArgumentException if the department code is empty or if n is less than or equal to 0
     * @throws EntityNotFoundException  if no department is found with the given code
     */
    @Transactional
    public ResponseEntity<?> getTopNVillesByPopulationOfDepartement(String codeDepartement, int n) {
        if (codeDepartement == null || codeDepartement.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Le code du département ne peut pas être vide");
        }

        if (n <= 0) {
            return ResponseEntity.badRequest().body("Le nombre de villes doit être supérieur à 0");
        }

        Departement dep = departementDao.getDepartementByCode(codeDepartement);
        if (dep == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Département avec le code " + codeDepartement + " non trouvé");
        }

        List<Ville> villes = villeDao.getLargestCitiesOfDepartement(codeDepartement, n);
        List<VilleDto> villesDto = villes.stream()
                .map(VilleMapper::toDto)
                .toList();

        return ResponseEntity.ok(villesDto);
    }

    /**
     * Retrieves Ville entities within a specified population range for a given department code.
     *
     * @param codeDepartement the code of the department
     * @param min             the minimum population
     * @param max             the maximum population
     * @return a list of Ville entities within the specified population range
     * @throws IllegalArgumentException if the department code is empty, if min or max are negative, or if min is greater than max
     * @throws EntityNotFoundException  if no department is found with the given code
     */
    @Transactional
    public ResponseEntity<?> getCitiesByPopulationRangeOfDepartement(String codeDepartement, int min, int max) {
        if (codeDepartement == null || codeDepartement.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Le code du département ne peut pas être vide");
        }

        if (min < 0 || max < 0) {
            return ResponseEntity.badRequest().body("Les valeurs de population doivent être positives");
        }

        if (min > max) {
            return ResponseEntity.badRequest().body("La valeur minimale ne peut pas être supérieure à la valeur maximale");
        }

        Departement dep = departementDao.getDepartementByCode(codeDepartement);
        if (dep == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Département avec le code " + codeDepartement + " non trouvé");
        }

        List<Ville> villes = villeDao.getCitiesByPopulationRangeOfDepartement(codeDepartement, min, max);
        List<VilleDto> villesDto = villes.stream()
                .map(VilleMapper::toDto)
                .toList();

        return ResponseEntity.ok(villesDto);
    }

}
