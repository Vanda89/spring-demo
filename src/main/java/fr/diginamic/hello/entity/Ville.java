package fr.diginamic.hello.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity

public class Ville {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "{message.id.positive}")
    private Integer id;

    @Column(name = "NOM")
    @NotNull(message = "{message.nom.notnull} ")
    @Size(min = 2, message = "{message.nom.size}")
    private String nom;

    @Column(name = "NB_HABITANTS")
    @Min(value = 1, message = "{message.nbHabitants.min}")
    private int nbHabitants;

    // Bidirectional ManyToOne relationship with Departement
    // JsonBackReference to handle serialization
    @ManyToOne(optional = false)
    @JoinColumn(name = "departement_id", nullable = false)
    @JsonBackReference
    private Departement departement;

    /* Default constructor for JPA */
    private Ville() {
    }

    /**
     * Constructor with parameters
     *
     * @param nom         Name of the city
     * @param nbHabitants Number of inhabitants
     * @param departement Associated department
     */
    public Ville(String nom, int nbHabitants, Departement departement) {
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.departement = departement;
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
        return "Ville{" +
                "nom='" + nom + '\'' +
                ", nbHabitants=" + nbHabitants +
                '}';
    }


}
