package vet_clinic_generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static vet_clinic_generator.OfficeGenerator.insertOffice;
import static vet_clinic_generator.ServiceOfficeGenerator.insertServiceOffice;
import static vet_clinic_generator.Utils.*;

    public class ServicesGenerator {

        private final static String SERVICES_PATH = "src/data/services.txt";

        private static int i = 1;

        public static void generate() {
            try {
                Files.readAllLines(Paths.get(SERVICES_PATH)).forEach(service -> {
                    String time = String.format("0%d:%d0:00", getRand(0, 3), getRand(0, 6));
                    insertService(service, time, getRand(20, 71));
                    i++; });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static void insertService(String service, String time, int amount_of_money) {
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.execute(String.format("INSERT INTO service (name_of_service, time_of_service_delivery, amount_of_money) VALUES (" +
                        "'%s','%s','%d00')", service, time, amount_of_money));
                insertOffice(statement, i);
                insertServiceOffice(statement, i);
            } catch (SQLException e) {
                System.out.println(DB_CONNECTING_ERROR);
                e.printStackTrace();
            }
        }
    }
