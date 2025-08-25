package fr.diginamic.hello.dtos;

public class VilleDto {
    private String codeVille;
    private String nom;
    private int nbHabitants;
    private String codeDepartement;
    private String nomDepartement;

    public VilleDto() {
    }

    /**
     *
     * @param codeVille
     * @param nom
     * @param nbHabitants
     * @param codeDepartement
     * @param nomDepartement
     */
    public VilleDto(String codeVille, String nom, int nbHabitants, String codeDepartement, String nomDepartement) {
        this.codeVille = codeVille;
        this.nom = nom;
        this.nbHabitants = nbHabitants;
        this.codeDepartement = codeDepartement;
        this.nomDepartement = nomDepartement;
    }

    public String getCodeVille() {
        return codeVille;
    }

    public void setCodeVille(String codeVille) {
        this.codeVille = codeVille;
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

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }
}
