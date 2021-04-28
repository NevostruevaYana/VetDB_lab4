package vet_clinic_generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import static vet_clinic_generator.Utils.*;

public class DrugGenerator {

    private final static String DRUGS_PATH = "src/data/drugs.txt";

    public static void generate() {
        try {
            Files.readAllLines(Paths.get(DRUGS_PATH)).forEach(drug_name -> {
                insertDrug(drug_name, getRand(0, 100));
            });
        } catch (IOException e) {
            System.out.println(FILE_NOT_FOUND_ERROR);
            e.printStackTrace();
        }
    }

    private static void insertDrug(String drug_name, int amount) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(String.format("INSERT INTO drug (drug_name, amount) VALUES (" +
                    "'%s','%d')", drug_name, amount));
        } catch (SQLException e) {
            System.out.println(DB_CONNECTING_ERROR);
            e.printStackTrace();
        }
    }
}