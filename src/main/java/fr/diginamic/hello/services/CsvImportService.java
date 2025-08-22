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
import java.util.List;

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

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] tokens = line.split(";");

                if (tokens.length < 8) continue;

                String codeDepartement = tokens[2].trim();
                String nom = tokens[6].trim();
                String nbHab = tokens[7].trim();

                Departement dep = departementDao.getDepartementByCode(codeDepartement);
                if (dep == null) {
                    dep = new Departement();
                    dep.setCode(codeDepartement);
                    departementDao.insertDepartement(dep);
                }

                if (nom.isEmpty() || nom.length() < 2 || nom.length() > 255) continue;
                int nbHabitants;
                try {
                    nbHabitants = Integer.parseInt(nbHab.replaceAll("\\s", ""));
                    if (nbHabitants < 1) continue;
                } catch (NumberFormatException e) {
                    continue;
                }

                Ville ville = new Ville(nom, nbHabitants, dep);


                villeDao.insertVille(ville);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du CSV", e);
        }
    }
}
