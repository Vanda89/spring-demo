package fr.diginamic.hello.dtos;

import fr.diginamic.hello.entity.Departement;

public class DepartementMapper {

    /**
     *
     * @param departement
     * @return
     */
    public static DepartementDto toDepartementDto(Departement departement) {
        return new DepartementDto(
                departement.getCode(),
                departement.getNom(),
                departement.getNombreHabitants()
        );
    }
}
