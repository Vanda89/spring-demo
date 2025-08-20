package fr.diginamic.hello.controllers;

import fr.diginamic.hello.entity.Ville;
import fr.diginamic.hello.repository.VilleRepository;
import fr.diginamic.hello.services.VilleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleController {


    @GetMapping
    public List<Ville> getVilles() {
        return Arrays.asList(
                new Ville("Paris", 2148000),
                new Ville("Lyon", 515695),
                new Ville("Marseille", 861635),
                new Ville("Bordeaux", 257068)
        );
    }


}
