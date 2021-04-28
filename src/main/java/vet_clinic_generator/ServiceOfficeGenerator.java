package vet_clinic_generator;

import java.sql.SQLException;
import java.sql.Statement;

import static vet_clinic_generator.Utils.*;

public class ServiceOfficeGenerator {

    public static void insertServiceOffice(Statement statement, int number) {
        try {
            statement.execute(String.format("INSERT INTO intersection_serv_office (service_id, office_id)" +
                    " VALUES ('%d','%d')", number, number));
        } catch (SQLException e) {
            System.out.println(DB_CONNECTING_ERROR);
            e.printStackTrace();
        }
    }

}
