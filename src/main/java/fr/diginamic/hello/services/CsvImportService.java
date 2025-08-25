package fr.diginamic.hello.services;

import fr.diginamic.hello.daos.VilleDao;
import fr.diginamic.hello.daos.DepartementDao;
import fr.diginamic.hello.entity.Ville;
import fr.diginamic.hello.entity.Departement;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CsvImportService {

    // The VilleDao is injected here to interact with the database
    private final VilleDao villeDao;
    // The DepartementDao is injected here to interact with the database
    private final DepartementDao departementDao;

    /**
     * Constructor to inject the VilleDao dependency.
     *
     * @param villeDao the VilleDao instance to be used by this service
     */
    public CsvImportService(VilleDao villeDao, DepartementDao departementDao) {
        this.villeDao = villeDao;
        this.departementDao = departementDao;
    }

    /**
     * Imports data from a CSV file into the database.
     *
     * @param csvFilePath the path to the CSV file
     */
    @Transactional
    public void importData(String csvFilePath) {
        List<Ville> villes = villeDao.getAllVilles();

        if (!villes.isEmpty()) {
            return;
        }
        Set<String> seenCodes = new HashSet<>();


        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split(";");

                if (tokens.length < 8) continue;

                String codeDepartement = tokens[2].trim();
                String codeVille = tokens[5].trim();
                String nom = tokens[6].trim();
                String nbHab = tokens[7].trim();
                String nbHabTotal = tokens[9].trim();

                if(codeDepartement.isEmpty()) continue;
                if (nom.length() < 2 || nom.length() > 255) continue;
                if (codeVille.isEmpty() || seenCodes.contains(codeVille)) continue;
                int nbHabitants;
                try {
                    nbHabitants = Integer.parseInt(nbHab.replaceAll("\\s", ""));
                    if (nbHabitants < 1) continue;
                } catch (NumberFormatException e) {
                    continue;
                }
                int nbHabitantsTotal;
                try {
                    nbHabitantsTotal = Integer.parseInt(nbHabTotal.replaceAll("\\s", ""));
                    if (nbHabitantsTotal < 1) continue;
                } catch (NumberFormatException e) {
                    continue;
                }

                Departement dep = departementDao.getDepartementByCode(codeDepartement);
                if (dep == null) {
                    dep = new Departement();
                    dep.setCode(codeDepartement);
                    dep.setNom("Departement " + codeDepartement);
                    dep.setNombreHabitants(nbHabitantsTotal);
                    departementDao.insertDepartement(dep);
                } else {
                    dep.setNombreHabitants(nbHabitantsTotal + dep.getNombreHabitants());
                    departementDao.updateDepartement(dep.getId(), dep);
                }

                Ville ville = new Ville(codeVille, nom, nbHabitants, dep);

                villeDao.insertVille(ville);
                seenCodes.add(codeVille);

            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du CSV", e);
        }
    }
}
