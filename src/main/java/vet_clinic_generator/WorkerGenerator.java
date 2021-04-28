package vet_clinic_generator;

import java.sql.*;

import static vet_clinic_generator.Utils.*;
import static vet_clinic_generator.WorkerOfficeGenerator.generateWorkerOffice;

public class WorkerGenerator {

    private final static String FULL_NAME_PATH = "src/data/full_name.txt";
    private final static String POSITION_PATH = "src/data/position.txt";

    public static void generate() {
        String full_name = getRandomData(FULL_NAME_PATH);
        String[] set_for_login = full_name.split(SPACE_SEPARATOR);
        String login = set_for_login[0].toLowerCase() + "." + set_for_login[1].toLowerCase().charAt(0)
                + set_for_login[2].toLowerCase().charAt(0);
        int password = passwordAndSN(8, "abcdefghijklmnoqrstvuwxyz").hashCode();
        String[] positionAndServ = getRandomData(POSITION_PATH).split(SPACE_SEPARATOR);
        String position = positionAndServ[0];
        String services = positionAndServ[1];
        insertWorker(full_name, login, password, position, getDate("for_worker"), phoneGenerator(), services);
    }

    private static void insertWorker(String full_name, String login, int password, String position,
                                    Date date_of_birth, String phone, String services) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            PreparedStatement ps = connection.prepareStatement(String.format("INSERT INTO worker (full_name, login, password, " +
                            "worker_position, date_of_birth, phone) VALUES ('%s','%s','%d','%s','%s','%s')",
                    full_name, login, password, position, date_of_birth, phone), Statement.RETURN_GENERATED_KEYS);
            ps.execute();
            ResultSet res = ps.getGeneratedKeys();
            if (res.next()) {
                int last_worker_id = res.getInt(1);
                generateWorkerOffice(statement, last_worker_id, services);
            }
        } catch (SQLException e) {
            System.out.println(DB_CONNECTING_ERROR);
            e.printStackTrace();
        }
    }

}
