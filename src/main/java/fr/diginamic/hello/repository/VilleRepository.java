package fr.diginamic.hello.repository;

import fr.diginamic.hello.entity.Ville;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Integer> {

    Page<Ville> findAll(Pageable pageable);

    Ville findByNomIgnoreCase(String nom);

    List<Ville> findByNomStartingWithIgnoreCase(String prefix);

    List<Ville> findByNbHabitantsGreaterThanOrderByNbHabitantsDesc(int min);

    List<Ville> findByNbHabitantsBetweenOrderByNbHabitantsDesc(int min, int max);

    List<Ville> findByDepartement_CodeAndNbHabitantsGreaterThanOrderByNbHabitantsDesc(String codeDepartement, int min);

    List<Ville> findByDepartement_CodeAndNbHabitantsBetweenOrderByNbHabitantsDesc(String codeDepartement, int min, int max);

    Page<Ville> findByDepartement_CodeOrderByNbHabitantsDesc(String codeDepartement, Pageable pageable);

}
