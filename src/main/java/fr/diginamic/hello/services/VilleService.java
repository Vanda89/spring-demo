package fr.diginamic.hello.services;

import fr.diginamic.hello.daos.DepartementDao;
import fr.diginamic.hello.entity.Departement;
import fr.diginamic.hello.entity.Ville;
import fr.diginamic.hello.daos.VilleDao;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @return a list of all Ville entities
     */
    @Transactional
    public List<Ville> extractVilles() {
        return villeDao.getAllVilles();
    }

    /**
     * Retrieves a Ville entity by its ID.
     *
     * @param id the ID of the Ville to retrieve
     * @return the Ville entity with the specified ID
     * @throws IllegalArgumentException if the ID is less than or equal to 0
     * @throws EntityNotFoundException if no Ville with the given ID exists
     */
    @Transactional
    public Ville extractVilleById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'id de la ville doit être supérieur à 0");
        }

        Ville ville = villeDao.getVilleById(id);

        if (ville == null) {
            throw new EntityNotFoundException("Ville avec l'id " + id + " non trouvée");
        }
        return ville;
    }

    /**
     * Retrieves a Ville entity by its name.
     *
     * @param nom the name of the Ville to search for
     * @return the Ville entity with the specified name
     * @throws IllegalArgumentException if the name is empty
     * @throws EntityNotFoundException if no Ville with the given name exists
     */
    @Transactional
    public Ville extractVilleByName(String nom) {
        if (nom == null || nom.isEmpty()) {
            throw new IllegalArgumentException("Le nom de la ville ne peut pas être vide");
        }

        Ville ville = villeDao.getVilleByName(nom);
        if (ville == null) {
            throw new EntityNotFoundException("Aucune ville trouvée avec le nom " + nom);
        }

        return ville;
    }

   /**   * Inserts a new Ville entity into the database.
     *
     * @param ville the Ville object to insert
     * @return a list of all Ville entities after the insertion
     * @throws IllegalArgumentException if the input data is invalid
     */
    @Transactional
    public List<Ville> insertVille(Ville ville) {

        if (ville == null) {
            throw new IllegalArgumentException("La ville ne peut pas être null");
        }

        if(ville.getNom().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la ville ne peut pas être vide");
        }

        if(ville.getNom().length() < 2 ) {
            throw new IllegalArgumentException("Le nom de la ville doit contenir au moins 2 caractères");
        }

        if(ville.getNbHabitants() < 1) {
            throw new IllegalArgumentException("Le nombre d'habitants doit être supérieur à 0");
        }

        Departement departement = departementDao.getDepartementByCode(ville.getDepartement().getCode());
        if(departement == null) {
            throw new IllegalArgumentException("Le département avec le code " + ville.getDepartement().getCode() + " n'existe pas");
        }

        ville.setDepartement(departement);

        villeDao.insertVille(ville);
        return villeDao.getAllVilles();
    }

    /**
     * Updates an existing Ville entity.
     *
     * @param idVille the ID of the Ville to update
     * @param villeUpdated the updated Ville object
     * @return a list of all Ville entities after the update
     * @throws IllegalArgumentException if no Ville with the given ID exists or if input data is invalid
     * @throws EntityNotFoundException if no Ville with the given ID exists
     */
    @Transactional
    public List<Ville> updateVille(int idVille, Ville villeUpdated) {
        if (idVille <= 0) {
            throw new IllegalArgumentException("L'id doit être strictement positif");
        }

        if (villeUpdated.getNom() == null || villeUpdated.getNom().trim().length() < 2)
            throw new IllegalArgumentException("Le nom de la ville doit contenir au moins 2 caractères");


        if (villeUpdated.getNbHabitants() < 1) {
            throw new IllegalArgumentException("Le nombre d'habitants doit être supérieur à 0");
        }

        if (villeUpdated.getDepartement() == null || villeUpdated.getDepartement().getCode() == null)
            throw new IllegalArgumentException("Le département doit être renseigné");

        Departement dep = departementDao.getDepartementByCode(villeUpdated.getDepartement().getCode());
        if (dep == null)
            throw new EntityNotFoundException("Département non trouvé pour code " + villeUpdated.getDepartement().getCode());

        villeDao.updateVille(idVille, villeUpdated);
        return villeDao.getAllVilles();

    }

  /**   * Deletes a Ville entity by its ID.
     *
     * @param idVille the ID of the Ville to delete
     * @throws IllegalArgumentException if no Ville with the given ID exists
     * @throws EntityNotFoundException if no Ville with the given ID exists
     */
    @Transactional
    public void deleteVille(int idVille) {
        if(idVille <= 0) {
            throw new IllegalArgumentException("L'id doit être strictement positif");
        }

        Ville ville = villeDao.getVilleById(idVille);
        if (ville == null) {
            throw new EntityNotFoundException("Ville avec l'id " + idVille + " non trouvée");
        }

        villeDao.deleteVille(idVille);
    }

    /**
     * Retrieves the top N Ville entities by population for a given department code.
     *
     * @param codeDepartement the code of the department
     * @param n the number of top cities to retrieve
     * @return a list of the top N Ville entities by population
     * @throws IllegalArgumentException if the department code is empty or if n is less than or equal to 0
     * @throws EntityNotFoundException if no department is found with the given code
     */
    @Transactional
    public List<Ville> getTopNVillesByPopulationOfDepartement(String codeDepartement, int n) {
        if (codeDepartement == null || codeDepartement.trim().isEmpty()) {
            throw new IllegalArgumentException("Le code du département ne peut pas être vide");
        }

        Departement departement = departementDao.getDepartementByCode(codeDepartement);
        if (departement == null) {
            throw new EntityNotFoundException("Département avec le code " + codeDepartement + " non trouvé");
        }

        if (n <= 0) {
            throw new IllegalArgumentException("Le nombre de villes doit être supérieur à 0");
        }

        return villeDao.getLargestCitiesOfDepartement(codeDepartement, n);
    }

    /**
     * Retrieves a list of Ville entities within a specified population range for a given department code.
     *
     * @param codeDepartement the code of the department
     * @param min the minimum population
     * @param max the maximum population
     * @return a list of Ville entities within the specified population range
     * @throws IllegalArgumentException if the department code is empty, or if min/max values are invalid
     * @throws EntityNotFoundException if no department is found with the given code
     */
    @Transactional
    public List<Ville> getCitiesByPopulationRangeOfDepartement (String codeDepartement, int min, int max) {
        if (codeDepartement == null || codeDepartement.trim().isEmpty()) {
            throw new IllegalArgumentException("Le code du département ne peut pas être vide");
        }

        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Les valeurs de population doivent être positives");
        }

        if (min > max) {
            throw new IllegalArgumentException("La valeur minimale ne peut pas être supérieure à la valeur maximale");
        }

        Departement departement = departementDao.getDepartementByCode(codeDepartement);
        if (departement == null) throw new EntityNotFoundException("Département non trouvé pour code " + codeDepartement);

        return villeDao.getCitiesByPopulationRangeOfDepartement(codeDepartement, min, max);
    }

}
