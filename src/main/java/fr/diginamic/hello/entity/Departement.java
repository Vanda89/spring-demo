package fr.diginamic.hello.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "DEPARTEMENT")
public class Departement {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Positive(message = "{departement.id.positive}")
    private Integer id;

    @Column(name = "CODE", unique = true)
    @NotNull(message = "{departement.code.notNull}")
    @Size(min = 2, message = "{departement.code.min}")
    private String code;

    @Column(name = "NOM")
    @NotNull(message = "{departement.nom.notnull")
    @Size(min = 2, message = "{departement.nom.min}")
    private String nom;

    @Column(name = "NB_HABITANTS")
    @Min(value = 1, message = "{departement.nombreHabitants.min}")
    private int nbHabitants;


    // Bidirectional OneToMany relationship with Ville
    // JsonManagedReference to handle serialization
    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Ville> villes = new ArrayList<>();

    public Departement() {
    }

    public Departement(String code, String nom, int nbHabitants) {
        this.code = code;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
    }

    /* Getters and Setters */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNombreHabitants() {
        return nbHabitants;
    }

    public void setNombreHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }


    public List<Ville> getVilles() {
        return villes;
    }

    public void setVilles(List<Ville> villes) {
        this.villes = villes;
    }


    /* Methods to manage the bidirectional relationship */
    public void addVille(Ville ville) {
        if (ville != null) {
            ville.setDepartement(this);
            villes.add(ville);
        }

    }

    public void removeVille(Ville ville) {
        if (ville != null) {
            ville.setDepartement(null);
            villes.remove(ville);
        }
    }

    @Override
    public String toString() {
        return STR."Departement{code='\{code}', nom='\{nom}', nbHabitants=\{nbHabitants}}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Departement that = (Departement) o;

        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }
}
