package fr.diginamic.hello.services;

import fr.diginamic.hello.entity.Departement;
import fr.diginamic.hello.daos.DepartementDao;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

public class DepartementService {

    // The DepartementDao is injected here to interact with the database
    private final DepartementDao departementDao;

    /**
     * Constructor to inject the DepartementDao dependency.
     *
     * @param departementDao the DepartementDao instance to be used by this service
     */
    public DepartementService(DepartementDao departementDao) {
        this.departementDao = departementDao;
    }

    /**
     * Retrieves all Departement entities from the database.
     *
     * @return a list of all Departement entities
     */
    @Transactional
    public List<Departement> extractDepartements() {
        return departementDao.getAllDepartement();
    }

    /**
     * Retrieves a Departement entity by its ID.
     *
     * @param id the ID of the Departement to retrieve
     * @return the Departement entity with the specified ID
     * @throws IllegalArgumentException if the ID is less than or equal to 0
     * @throws EntityNotFoundException if no Departement with the given ID exists
     */
    @Transactional
    public Departement extractDepartementById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'id du département doit être supérieur à 0");
        }

        Departement departement = departementDao.getDepartementById(id);

        if (departement == null) {
            throw new EntityNotFoundException("Département avec l'id " + id + " non trouvé");
        }
        return departement;
    }

    /**
     * Retrieves a Departement entity by its code.
     *
     * @param code the code of the Departement to retrieve
     * @return the Departement entity with the specified code
     * @throws IllegalArgumentException if the code is null or empty
     * @throws EntityNotFoundException if no Departement with the given code exists
     */
    @Transactional
    public Departement extractDepartementByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Le code du département ne peut pas être vide");
        }

        Departement departement = departementDao.getDepartementByCode(code);
        if (departement == null) {
            throw new EntityNotFoundException("Département avec le code " + code + " non trouvé");
        }
        return departement;
    }

    /**
     * Inserts a new Departement entity into the database.
     *
     * @param departement the Departement object to insert
     * @return the updated list of all Departement entities
     * @throws IllegalArgumentException if the departement is null or its code is null or empty
     */
    @Transactional
    public List<Departement> insertDepartement(Departement departement) {
        if (departement == null) {
            throw new IllegalArgumentException("Le département ne peut pas être null");
        }
        if (departement.getCode() == null || departement.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Le code du département ne peut pas être vide");
        }

        departementDao.insertDepartement(departement);
        return departementDao.getAllDepartement();
    }

    /**
     * Updates an existing Departement entity.
     *
     * @param idDepartement the ID of the Departement to update
     * @param departementUpdated the updated Departement object
     * @return the updated list of all Departement entities
     * @throws IllegalArgumentException if the ID is less than or equal to 0 or if the updated departement's code is null or empty
     */
    @Transactional
    public List<Departement> updateDepartement(int idDepartement, Departement departementUpdated) {
        if (idDepartement <= 0) {
            throw new IllegalArgumentException("L'id du département doit être supérieur à 0");
        }

        if (departementUpdated.getCode() == null || departementUpdated.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Le code du département ne peut pas être vide");
        }

        departementDao.updateDepartement(idDepartement, departementUpdated);
        return departementDao.getAllDepartement();
    }

    /**
     * Deletes a Departement entity by its ID.
     *
     * @param idDepartement the ID of the Departement to delete
     * @return the updated list of all Departement entities
     * @throws IllegalArgumentException if the ID is less than or equal to 0
     */
    @Transactional
    public List<Departement> deleteDepartement(int idDepartement) {
        if (idDepartement <= 0) {
            throw new IllegalArgumentException("L'id du département doit être supérieur à 0");
        }

        departementDao.deleteDepartement(idDepartement);
        return departementDao.getAllDepartement();
    }
}
