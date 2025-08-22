package fr.diginamic.hello.daos;

import fr.diginamic.hello.entity.Departement;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartementDao {

    @PersistenceContext
    private EntityManager em;

    /**
     * Retrieve all Departement entities from the database.
     *
     * @return
     */
    public List<Departement> getAllDepartement() {
        return em.createQuery("SELECT d FROM Departement d", Departement.class).getResultList();
    }

    /**
     * Retrieve a Departement entity by its ID.
     *
     * @param id the ID of the Departement to retrieve
     * @return the Departement entity with the specified ID, or null if not found
     */
    public Departement getDepartementById(int id) {
        return em.find(Departement.class, id);
    }

    /**
     * Retrieve a Departement entity by its code.
     *
     * @param code the code of the Departement to search for
     * @return the Departement entity with the specified code, or null if not found
     */
    public Departement getDepartementByCode(String code) {
        try {
            return em.createQuery(
                            "SELECT d FROM Departement d WHERE d.code = :code", Departement.class)
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Inserts a new Departement entity into the database.
     *
     * @param departement the Departement object to insert
     * @throws IllegalArgumentException if a Departement with the same code already exists
     */
    public void insertDepartement(Departement departement) {
        em.persist(departement);
    }

    /**
     * Updates an existing Departement entity.
     *
     * @param idDepartement      the ID of the Departement to update
     * @param departementUpdated the Departement object containing updated data
     * @throws IllegalArgumentException if the Departement with the specified ID does not exist
     */
    public void updateDepartement(int idDepartement, Departement departementUpdated) {
        Departement villeExisting = em.find(Departement.class, idDepartement);

        if (villeExisting == null) {
            throw new IllegalArgumentException("Departement with id " + idDepartement + " does not exist.");
        }

        villeExisting.setCode(departementUpdated.getCode());
    }

    /**
     * Deletes a Departement entity by its ID.
     *
     * @param idDepartement the ID of the Departement to delete
     * @throws IllegalArgumentException if the Departement with the specified ID does not exist
     */
    public void deleteDepartement(int idDepartement) {
        Departement departement = em.find(Departement.class, idDepartement);

        if (departement == null) {
            throw new IllegalArgumentException("Departement with id " + idDepartement + " does not exist.");
        } else {
            em.remove(departement);
        }
    }

}
