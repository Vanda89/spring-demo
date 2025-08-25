package fr.diginamic.hello.services;

import fr.diginamic.hello.dtos.VilleDto;
import fr.diginamic.hello.dtos.VilleMapper;
import fr.diginamic.hello.entity.Departement;
import fr.diginamic.hello.entity.Ville;
import fr.diginamic.hello.repository.DepartementRepository;
import fr.diginamic.hello.repository.VilleRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.awt.print.Pageable;
import java.util.List;

@Service
public class VilleService {

    private final VilleRepository villeRepository;
    private final DepartementRepository departementRepository;


    /**
     * @param villeRepository
     * @param departementRepository
     */
    public VilleService(VilleRepository villeRepository, DepartementRepository departementRepository) {

        this.villeRepository = villeRepository;
        this.departementRepository = departementRepository;
    }

    /**
     * Retrieves all the cities sorted by page
     *
     * @param page
     * @param size
     * @return ResponseEntity containing a paginated list of Ville entities or a message if no cities are found
     */
    @Transactional
    public ResponseEntity<?> extractVilles(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Ville> villes = villeRepository.findAll(pageRequest);

        if (villes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Aucune ville trouvée");
        }

        Page<VilleDto> villesDto = villes.map(VilleMapper::toDto);
        return ResponseEntity.ok(villesDto);
    }

    /**
     * Retrieves a Ville entity by its ID.
     *
     * @param id the ID of the Ville to retrieve
     * @return ResponseEntity containing the Ville entity with the specified ID
     */
    @Transactional
    public ResponseEntity<?> extractVilleById(int id) {
        if (id <= 0) {
            return ResponseEntity.badRequest().body("L'id de la ville doit être supérieur à 0");
        }

        Ville ville = villeRepository.findById(id).orElse(null);
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
     * @return ResponseEntity containing the Ville entity with the specified name
     */
    @Transactional
    public ResponseEntity<?> extractVilleByName(String nom) {
        if (nom == null || nom.isEmpty()) {
            return ResponseEntity.badRequest().body("Le nom de la ville ne peut pas être vide");
        }
        Ville ville = villeRepository.findByNomIgnoreCase(nom);
        if (ville == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucune ville trouvée avec le nom " + nom);
        }

        VilleDto villeDto = VilleMapper.toDto(ville);
        return ResponseEntity.ok(villeDto);
    }

    /**
     * Retrieves a list of Ville starting by a specified prefix
     *
     * @param prefix
     * @return ResponseEntity containing a list of villes starting by a specified string
     */
    @Transactional
    public ResponseEntity<?> extractVilleStartsWith(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Ville> villes = villeRepository.findByNomStartingWithIgnoreCase(prefix);
        if (villes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(villes);
        }

        List<VilleDto> villesDto = villes.stream()
                .map(VilleMapper::toDto)
                .toList();
        return ResponseEntity.ok(villesDto);
    }

    /**
     * Inserts a new Ville entity into the database.
     *
     * @param ville the Ville object to insert
     * @return ResponseEntity containing a list of all Ville entities after the insertion or an error message
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

        Departement dep = departementRepository.findByCode(ville.getDepartement().getCode());
        if (dep == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Département avec le code " + ville.getDepartement().getCode() + " non trouvé");
        }

        ville.setDepartement(dep);
        villeRepository.save(ville);

        List<Ville> villes = villeRepository.findAll();
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
     * @param result       the validation result (BindingResult)
     * @return ResponseEntity containing a list of all Ville entities after insertion or an error message
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

        Ville existingVille = villeRepository.findById(idVille).orElse(null);
        if (existingVille == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ville avec l'id " + idVille + " non trouvée");
        }

        Departement dep = departementRepository.findByCode(villeUpdated.getDepartement().getCode());
        if (dep == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Département non trouvé pour code " + villeUpdated.getDepartement().getCode());
        }

        existingVille.setNom(villeUpdated.getNom());
        existingVille.setNbHabitants(villeUpdated.getNbHabitants());
        existingVille.setDepartement(dep);
        existingVille.setCodeVille(villeUpdated.getCodeVille());

        villeRepository.save(existingVille);

        List<Ville> villes = villeRepository.findAll();
        List<VilleDto> villesDto = villes.stream()
                .map(VilleMapper::toDto)
                .toList();

        return ResponseEntity.ok(villesDto);
    }

    /**
     * Deletes a Ville entity by its ID.
     *
     * @param idVille the ID of the Ville to delete
     * @return ResponseEntity contains a success or an error message if the city does not exist
     */
    @Transactional
    public ResponseEntity<?> deleteVille(int idVille) {
        if (idVille <= 0) {
            return ResponseEntity.badRequest().body("L'id doit être strictement positif");
        }
        return villeRepository.findById(idVille).map(ville -> {
            villeRepository.delete(ville);
            return ResponseEntity.ok("Ville supprimée avec succès avec l'id : " + idVille);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Ville avec l'id " + idVille + " non trouvée"));
    }


    /**
     * Retrieves the top N Ville entities by population.
     *
     * @param min the number of top cities to retrieve
     * @return ResponseEntity contains a list of the top N Ville entities by population
     */
    @Transactional
    public ResponseEntity<?> getTopNCitiesByPopulation(int min) {
        if (min <= 0) {
            return ResponseEntity.badRequest().body("Le nombre de villes doit être supérieur à 0");
        }

        List<Ville> villes = villeRepository.findByNbHabitantsGreaterThanOrderByNbHabitantsDesc(min);
        if (villes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }

        List<VilleDto> villesDto = villes.stream()
                .map(VilleMapper::toDto)
                .toList();

        return ResponseEntity.ok(villesDto);
    }

    /**
     * Retrieves Ville entities within a specified population range
     *
     * @param min the minimum population
     * @param max the maximum population
     * @return ResponseEntity contains a list of Ville entities within the specified population range
     */
    @Transactional
    public ResponseEntity<?> getCitiesByPopulationRange(int min, int max) {
        if (min < 0 || max < 0) {
            return ResponseEntity.badRequest().body("Les valeurs de population doivent être positives");
        }

        if (min > max) {
            return ResponseEntity.badRequest().body("La valeur minimale ne peut pas être supérieure à la valeur maximale");
        }

        List<Ville> villes = villeRepository.findByNbHabitantsBetweenOrderByNbHabitantsDesc(min, max);
        List<VilleDto> villeDto = villes.stream().map(VilleMapper::toDto).toList();

        return ResponseEntity.ok(villeDto);
    }

    /**
     * Retrieves the top N Ville entities by population of a specific Departement
     *
     * @param codeDepartement the code of the department
     * @param min             the number of top cities to retrieve
     * @return ResponseEntity contains a list of the top N Ville entities by population of a specific Departement
     */
    public ResponseEntity<?> getTopNCitiesByPopulationOfDepartment(String codeDepartement, int min) {
        if (codeDepartement == null || codeDepartement.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Le code du département ne peut pas être vide");
        }

        if (min <= 0) {
            return ResponseEntity.badRequest().body("Le nombre de villes doit être supérieur à 0");
        }

        List<Ville> villes = villeRepository.findByDepartement_CodeAndNbHabitantsGreaterThanOrderByNbHabitantsDesc(codeDepartement, min);
        List<VilleDto> villeDto = villes.stream().map(VilleMapper::toDto).toList();

        return ResponseEntity.ok(villeDto);
    }

    /**
     * Retrieves Ville entities within a specified population range for a given department code.
     *
     * @param codeDepartement the code of the department
     * @param min             the minimum population
     * @param max             the maximum population
     * @return ResponseEntity contains a list of Ville entities within the specified population range of a specific Departement
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


        List<Ville> villes = villeRepository.findByDepartement_CodeAndNbHabitantsBetweenOrderByNbHabitantsDesc(codeDepartement, min, max);
        List<VilleDto> villesDto = villes.stream()
                .map(VilleMapper::toDto)
                .toList();

        return ResponseEntity.ok(villesDto);
    }


    /**
     * Retrieves the top N Ville entities by population for a given department code.
     *
     * @param codeDepartement the code of the department
     * @param n               the number of top cities to retrieve
     * @return ResponseEntity contains a paginated list of the top N Ville entities by population of a Department
     */
    @Transactional
    public ResponseEntity<?> getTopNCitiesByPopulationOfDepartement(String codeDepartement, int n) {
        if (codeDepartement == null || codeDepartement.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Le code du département ne peut pas être vide");
        }

        if (n <= 0) {
            return ResponseEntity.badRequest().body("Le nombre de villes doit être supérieur à 0");
        }

        PageRequest topN = PageRequest.of(0, n);
        Page<Ville> villes = villeRepository.findByDepartement_CodeOrderByNbHabitantsDesc(codeDepartement, topN);
        List<VilleDto> villesDto = villes.getContent().stream()
                .map(VilleMapper::toDto)
                .toList();

        return ResponseEntity.ok(villesDto);
    }


}
