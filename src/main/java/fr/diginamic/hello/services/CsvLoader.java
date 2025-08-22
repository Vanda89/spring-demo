package fr.diginamic.hello.services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CsvLoader implements CommandLineRunner {

    private final CsvImportService csvImportService;

    public CsvLoader(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @Override
    public void run(String... args) throws Exception {
        csvImportService.importData("src/main/resources/data/recensement.csv");
    }
}
