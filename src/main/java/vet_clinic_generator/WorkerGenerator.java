package vet_clinic_generator;

import org.postgresql.util.Base64;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

import static vet_clinic_generator.Utils.*;
import static vet_clinic_generator.WorkerOfficeGenerator.generateWorkerOffice;

public class WorkerGenerator {

    private final static String FULL_NAME_PATH = "src/data/full_name.txt";
    private final static String POSITION_PATH = "src/data/position.txt";

    public static void generate() {
        String full_name = getRandomData(FULL_NAME_PATH);
        String[] set_for_login = full_name.split(SPACE_SEPARATOR);
        String login = generatePasswordForEquipment(8, "abcdefghijklmnoqrstvuwxyz");
//                set_for_login[0].toLowerCase() + "." + set_for_login[1].toLowerCase().charAt(0)
//                + set_for_login[2].toLowerCase().charAt(0);
        char[] password_ = generatePasswordForWorkers(8, "abcdefghijklmnoqrstvuwxyz");
        try (FileWriter writer = new FileWriter("src/out_data/" + full_name + ".txt", false)) {
            for (char c : password_) writer.write(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] salt_psw = getSalt();
        byte[] hash_psw = getHash(password_, salt_psw);
        String salt = Base64.encodeBytes(salt_psw);
        String password = Base64.encodeBytes(hash_psw);
        String[] positionAndServ = getRandomData(POSITION_PATH).split(SPACE_SEPARATOR);
        String position = positionAndServ[0];
        String services = positionAndServ[1];
        insertWorker(full_name, login, salt, password, position, getDate("for_worker"), phoneGenerator(), services);
    }

    private static void insertWorker(String full_name, String login, String salt, String password, String position,
                                    Date date_of_birth, String phone, String services) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            PreparedStatement ps = connection.prepareStatement(String.format("INSERT INTO worker (full_name, login, password, salt, " +
                            "worker_position, date_of_birth, phone) VALUES ('%s','%s','%s','%s','%s','%s','%s')",
                    full_name, login, password, salt, position, date_of_birth, phone), Statement.RETURN_GENERATED_KEYS);
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
