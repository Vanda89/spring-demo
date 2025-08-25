package fr.diginamic.hello.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Objects;

@Entity
@Table(name = "VILLE")
public class Ville {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "{ville.id.positive}")
    private Integer id;

    @Column(name = "CODE_VILLE", unique = true)
    @NotNull(message = "{ville.codeVille.notNull}")
    @Size(min = 2, message = "{ville.codeVille.min}")
    private String codeVille;

    @Column(name = "NOM")
    @NotNull(message = "{ville.nom.notNull} ")
    @Size(min = 2, message = "{ville.nom.min}")
    private String nom;

    @Column(name = "NB_HABITANTS")
    @Min(value = 1, message = "{ville.nbHabitants.min}")
    private int nbHabitants;

    // Bidirectional ManyToOne relationship with Departement
    // JsonBackReference to handle serialization
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;

    /**
     * Constructor with parameters
     *
     * @param codeVille   Code of the city
     * @param nom         Name of the city
     * @param nbHabitants Number of inhabitants
     * @param departement Associated department
     */
    public Ville(String codeVille, String nom, int nbHabitants, Departement departement) {
        this.codeVille = codeVille;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.departement = departement;
    }

    // Default constructor
    public Ville() {

    }

    /* Getters and Setters */
    public Departement getDepartement() {
        return departement;
    }

    /* Setter that manages the bidirectional relationship */
    public void setDepartement(Departement departement) {
        if (this.departement != null) {
            this.departement.getVilles().remove(this);
        }
        this.departement = departement;
        if (departement != null && !departement.getVilles().contains(this)) {
            departement.getVilles().add(this);
        }
    }

    public String getCodeVille() {
        return codeVille;
    }

    public void setCodeVille(String codeVille) {
        this.codeVille = codeVille;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbHabitants() {
        return nbHabitants;
    }

    public void setNbHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }

    @Override
    public String toString() {
        return STR."Ville{nom='\{nom}', nbHabitants=\{nbHabitants}}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ville ville = (Ville) o;

        return Objects.equals(id, ville.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
