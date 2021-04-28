package vet_clinic_generator;

import java.sql.SQLException;
import java.sql.Statement;

import static vet_clinic_generator.Utils.*;

public class OfficeGenerator {

    public static void insertOffice(Statement statement, int number_of_office) {
        try {
            statement.execute(String.format("INSERT INTO office (number_of_office) VALUES ('%d')", number_of_office));
        } catch (SQLException e) {
            System.out.println(DB_CONNECTING_ERROR);
            e.printStackTrace();
        }
    }
}
