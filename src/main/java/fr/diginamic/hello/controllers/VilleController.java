package fr.diginamic.hello.controllers;

import fr.diginamic.hello.entity.Ville;
import fr.diginamic.hello.repository.VilleRepository;
import fr.diginamic.hello.services.VilleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleController {

    private final List<Ville> villes = new ArrayList<>();

    public VilleController() {
        villes.add(new Ville("Paris", 2148000));
        villes.add(new Ville("Lyon", 515695));
        villes.add(new Ville("Marseille", 861635));
        villes.add(new Ville("Bordeaux", 257068));
    }


    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }


}
