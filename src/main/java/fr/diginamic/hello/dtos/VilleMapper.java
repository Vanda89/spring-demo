package fr.diginamic.hello.dtos;

import fr.diginamic.hello.entity.Departement;
import fr.diginamic.hello.entity.Ville;
import org.springframework.stereotype.Component;

@Component
public class VilleMapper {

    /**
     *
     * @param ville
     * @return - a VilleDto containing the Ville's code, name, population,
     * department code, and department name
     * @throws IllegalArgumentException if the input ville is null
     */
    public static VilleDto toDto(Ville ville) {
        if (ville == null) {
            throw new IllegalArgumentException("Ville entity cannot be null");
        }

        return new VilleDto(
                ville.getCodeVille(),
                ville.getNom(),
                ville.getNbHabitants(),
                ville.getDepartement().getCode(),
                ville.getDepartement().getNom()
        );
    }
}

