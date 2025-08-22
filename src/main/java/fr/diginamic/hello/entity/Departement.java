package fr.diginamic.hello.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Departement {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Positive(message = "{departement.id.positive}")
    private Integer id;

    @NotNull(message = "{departement.code.notnull}")
    @Size(min = 2, message = "{departement.code.min}")
    private String code;

    // Bidirectional OneToMany relationship with Ville
    // JsonManagedReference to handle serialization
    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Ville> villes = new ArrayList<>();

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
}
