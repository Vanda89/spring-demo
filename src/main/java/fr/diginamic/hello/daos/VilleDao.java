package fr.diginamic.hello.daos;


import fr.diginamic.hello.entity.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VilleDao {

    // EntityManager is used to interact with the persistence context
    // It allows us to perform CRUD operations on the database
    @PersistenceContext
    private EntityManager em;

    /**
     * Retrieves all Ville entities from the database.
     *
     * @return a list of all Ville entities
     */
    public List<Ville> getAllVilles() {
        return em.createQuery("SELECT v FROM Ville v", Ville.class).getResultList();
    }

    /**
     * Retrieves a Ville entity by its ID.
     *
     * @param id the ID of the Ville to retrieve
     * @return the Ville entity with the specified ID, or null if not found
     */
    public Ville getVilleById(int id) {
        return em.find(Ville.class, id);
    }

    /**
     * Retrieves a list of Ville entities by their name.
     *
     * @param nom the name of the Ville to search for
     * @return a list of Ville entities with the specified name
     */
    public Ville getVilleByName(String nom) {
        return em.createQuery("SELECT v FROM Ville v WHERE v.nom = :nom", Ville.class)
                .setParameter("nom", nom)
                .getSingleResult();
    }

    /**
     * Inserts a new Ville entity into the database.
     *
     * @param ville the Ville object to insert
     */
    public void insertVille(Ville ville) {
        em.persist(ville);
    }


    /**
     * Updates an existing Ville entity.
     *
     * @param idVille      the ID of the Ville to update
     * @param villeUpdated the updated Ville object
     * @throws IllegalArgumentException if no Ville with the given ID exists
     */
    public void updateVille(int idVille, Ville villeUpdated) {
        Ville villeExisting = em.find(Ville.class, idVille);
        if (villeExisting == null) {
            throw new IllegalArgumentException("Ville avec l'id " + idVille + " non trouvée");
        }

        villeExisting.setCodeVille(villeUpdated.getCodeVille());
        villeExisting.setNom(villeUpdated.getNom());
        villeExisting.setNbHabitants(villeUpdated.getNbHabitants());
        villeExisting.setDepartement(villeUpdated.getDepartement());

    }

    /**
     * Deletes a Ville entity by its ID.
     *
     * @param idVille the ID of the Ville to delete
     * @throws IllegalArgumentException if no Ville with the given ID exists
     */
    public void deleteVille(int idVille) {
        Ville ville = em.find(Ville.class, idVille);

        if (ville == null) {
            throw new IllegalArgumentException("Ville avec l'id " + idVille + " non trouvée");
        }
        em.remove(ville);

    }


    /**
     *
     * @param codeDepartement
     * @param n
     * @return
     */
    public List<Ville> getLargestCitiesOfDepartement(String codeDepartement, int n) {
        return em.createQuery("SELECT v FROM Ville v WHERE v.departement.code = :codeDepartement ORDER BY v.nbHabitants DESC", Ville.class)
                .setParameter("codeDepartement", codeDepartement)
                .setMaxResults(n)
                .getResultList();
    }

    public List<Ville> getCitiesByPopulationRangeOfDepartement(String codeDepartement, int min, int max) {
        return em.createQuery("SELECT v FROM Ville v WHERE (v.nbHabitants BETWEEN :min AND :max) AND v.departement.code = :codeDepartement ORDER BY v.nbHabitants DESC", Ville.class)
                .setParameter("codeDepartement", codeDepartement)
                .setParameter("min", min)
                .setParameter("max", max)
                .getResultList();
    }
}