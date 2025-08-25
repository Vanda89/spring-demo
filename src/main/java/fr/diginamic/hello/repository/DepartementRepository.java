package fr.diginamic.hello.repository;

import fr.diginamic.hello.entity.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Integer> {

    Departement findByCode(String code);
}