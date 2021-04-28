package vet_clinic_generator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import static vet_clinic_generator.Utils.*;

public class DrugRequestGenerator {

    public static void generate() {
        ArrayList<Date> dates = getDateRequestAndSupply();
        insertDrugRequest(getRand(0, 100), dates.get(0), dates.get(1));
    }

    private static void insertDrugRequest(int amount, Date date_of_request, Date date_of_supply) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ArrayList<String> workers_id = returnValues(statement, "id", "worker");
            ArrayList<String> drugs_id = returnValues(statement, "id", "drug");
            statement.execute(String.format("INSERT INTO drug_request (requesting_worker_id, drug_id, " +
                            "amount, date_of_request, date_of_supply) VALUES ('%s','%s','%d','%s','%s')",
                    workers_id.get(getRand(0, workers_id.size())), drugs_id.get(getRand(0, drugs_id.size())),
                    amount, date_of_request, date_of_supply));
        } catch (SQLException e) {
            System.out.println(DB_CONNECTING_ERROR);
            e.printStackTrace();
        }
    }
}
