package vet_clinic_generator;

import transaction.Client;

import java.sql.*;
import java.util.ArrayList;

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
