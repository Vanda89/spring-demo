package fr.diginamic.hello.dtos;


import fr.diginamic.hello.entity.Ville;

public class DepartementDto {

    private String codeDepartement;
    private String nom;
    private int nbHabitants;

    public DepartementDto(String codeDepartement, String nom, int nbHabitants) {
        this.codeDepartement = codeDepartement;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
    }



    /* Getters and Setters */
    public String getCode() {
        return codeDepartement;
    }

    public void setCode(String codeDepartement) {
        this.codeDepartement = codeDepartement;
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
}