package vet_clinic_generator;

import transaction.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static vet_clinic_generator.PetGenerator.generatePet;
import static vet_clinic_generator.Utils.*;

public class ClientGenerator {

    private final static String FULL_NAME_PATH = "src/data/full_name.txt";
    private final static String STREET_PATH = "src/data/address(SP-streets).txt";

    public static void generate() {
        String full_name = getRandomData(FULL_NAME_PATH);
        String address = String.format("г. Санкт-Петербург, %s, д. %d, кв. %d",
                getRandomData(STREET_PATH), getRand(1, 100), getRand(1, 700));
        boolean regular = getRand(0, 2) == 1;
        insertClient(full_name, address, phoneGenerator(), regular);
    }

    public static ArrayList<Client> returnGeneratedClients(int quantity) {
        ArrayList<Client> clients = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            String full_name = getRandomData(FULL_NAME_PATH);
            String address = String.format("г. Санкт-Петербург, %s, д. %d, кв. %d",
                    getRandomData(STREET_PATH), getRand(1, 100), getRand(1, 700));
            boolean regular = getRand(0, 2) == 1;
            clients.add(new Client(full_name, address, phoneGeneratorForTransaction(), regular));
        }
        return clients;
    }

    public static List<Long> insertClientForTransaction(ArrayList<Client> clients, Connection connection) {
        List<Long> insert_time = new ArrayList<>();
        AtomicBoolean flag = new AtomicBoolean(false);
        for (Client client : clients) {
            long start = System.nanoTime();
            do {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(String.format("INSERT INTO client (full_name, address, phone, regular) VALUES (" +
                            "'%s','%s','%s','%b')", client.getFull_name(), client.getAddress(), client.getPhone(), false));
                    flag.set(false);
                } catch (SQLException e) {
                    if (e.getSQLState().equals(SERIALIZABLE_ERROR))
                        flag.set(true);
                }
            } while (flag.get());
            insert_time.add(System.nanoTime() - start);
        }
        return insert_time;
    }

    public static List<Long> selectClientForTransaction(int num_of_iterations, Connection connection) {
        List<Long> select_time = new ArrayList<>();
        //List<Long> selected_lines_counter = new ArrayList<>();
        AtomicBoolean flag = new AtomicBoolean(false);
        //int number_of_selected_lines = 0;
        for (int i = 0; i < num_of_iterations; i++) {
                long start = System.nanoTime();
                do {
                    try (Statement statement = connection.createStatement()) {
                        statement.execute("SELECT * FROM client WHERE regular = false");
                        //ResultSet res = statement.executeQuery("SELECT * FROM client WHERE regular = false");
                        //while (res.next())
                            //number_of_selected_lines++;
                        flag.set(false);
                    } catch (SQLException e) {
                        if (e.getSQLState().equals(SERIALIZABLE_ERROR))
                            flag.set(true);
                    }
                } while (flag.get());
                select_time.add(System.nanoTime() - start);
                //selected_lines_counter.add(Long.valueOf(number_of_selected_lines));
                //number_of_selected_lines = 0;
            }
        return select_time;
        //return selected_lines_counter;
    }

    public static List<Long> updateClientForTransaction(int num_of_iterations, Connection connection) {
        List<Long> update_time = new ArrayList<>();
        //List<Long> updated_lines_counter = new ArrayList<>();
        AtomicBoolean flag = new AtomicBoolean(false);
        //int number_of_updated_lines = 0;
        for (int i = 0; i < num_of_iterations; i++) {
                long start = System.nanoTime();
                do {
                    try (Statement statement = connection.createStatement()) {
                        statement.executeUpdate("UPDATE client SET regular = true WHERE phone LIKE '%7'");
                        //number_of_updated_lines = statement.executeUpdate("UPDATE client SET regular = true WHERE phone LIKE '%7'");
                        flag.set(false);
                    } catch (SQLException e) {
                        if (e.getSQLState().equals(SERIALIZABLE_ERROR))
                            flag.set(true);
                    }
                } while (flag.get());
                update_time.add(System.nanoTime() - start);
                //updated_lines_counter.add(Long.valueOf(number_of_updated_lines));
                //number_of_updated_lines = 0;
            }
        return update_time;
        //return updated_lines_counter;
    }

    private static void insertClient(String full_name, String address, String phone, boolean regular) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            PreparedStatement ps = connection.prepareStatement(String.format("INSERT INTO client (full_name, address, phone, regular) VALUES (" +
                    "'%s','%s','%s','%b')", full_name, address, phone, regular), Statement.RETURN_GENERATED_KEYS);
            ps.execute();
            ResultSet res = ps.getGeneratedKeys();
            if (res.next()) {
                int last_client_id = res.getInt(1);
                generatePet(last_client_id, statement);
            }
        } catch (SQLException e) {
            System.out.println(DB_CONNECTING_ERROR);
            e.printStackTrace();
        }
    }

}
