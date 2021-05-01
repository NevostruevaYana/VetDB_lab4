package vet_clinic_generator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Utils {

    final static String SPACE_SEPARATOR = " ";
    final static String COMMA_SEPARATOR = ",";
    public final static String DB_CONNECTING_ERROR = "Error while connecting to DB";
    final static String FILE_NOT_FOUND_ERROR = "File not found";
    public final static String SERIALIZABLE_ERROR = "40001";

    final static String USER = "postgres";
    final static String password = "12345";
    final static String URL = "jdbc:postgresql://localhost:5432/vet_clinic_db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, password);
        } catch (SQLException e) {
            System.out.println(DB_CONNECTING_ERROR);
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConnection(int isolationLvl) {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.setTransactionIsolation(isolationLvl);
            } catch (SQLException e) {
                System.out.println(Utils.DB_CONNECTING_ERROR);
                e.printStackTrace();
            }
        }
        return  connection;
    }

    public static void deleteAllInfo() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM equipment;" +
                    "DELETE FROM drug_request;\n" +
                    "DELETE FROM drug;\n" +
                    "DELETE FROM intersection_schedule_office;\n" +
                    "DELETE FROM intersection_schedule_service;\n" +
                    "DELETE FROM schedule_of_visit;\n" +
                    "DELETE FROM intersection_serv_office;\n" +
                    "DELETE FROM intersection_worker_office;\n" +
                    "DELETE FROM office;\n" +
                    "DELETE FROM service;\n" +
                    "DELETE FROM worker;\n" +
                    "DELETE FROM pet;\n" +
                    "DELETE FROM client;");
        } catch (SQLException e) {
            System.out.println(DB_CONNECTING_ERROR);
            e.printStackTrace();
        }
    }

    public static int getRand(int a, int b) {
        return (int) (Math.random() * (b-a)) + a;
    }

    public static String phoneGenerator() {
        return String.format("+7(9%d)%d-%d-%d", getRand(10, 99), getRand(100, 999),
                getRand(10, 99), getRand(10, 99));
    }

    public static String phoneGeneratorForTransaction() {
        return String.format("+7(9%d)%d-%d-%d", getRand(10, 99), getRand(100, 999),
                getRand(10, 99), 67);
    }

    public static String getRandomData(String path) {
        try {
            return Files.readAllLines(Paths.get(path)).get(getRand(0, lineCounter(path)));
        } catch (IOException e) {
            System.out.println(FILE_NOT_FOUND_ERROR);
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDate(String str) {
        long begin_date;
        long period;
        int month_or_year = 365;
        switch (str) {
            case ("for_pet") -> {
                begin_date = 1336284441411L;
                period = 8L;
            }
            case ("for_schedule") -> {
                begin_date = 1517230000000L;  //1617230000000L
                period = 3L;
                month_or_year = 365; //12
            }
            case ("for_worker") -> {
                begin_date = -94677120000L;
                period = 35L;
            }
            default -> {
                begin_date = 6025276443752L;
                period = 30L;
            }
        }
        Random rnd = new Random();
        long ms = begin_date + (Math.abs(rnd.nextLong()) % (period * month_or_year * 24 * 60 * 60 * 1000));
        return new Date(ms);
    }

    public static ArrayList<Date> getDateRequestAndSupply() {
        ArrayList<Date> dates = new ArrayList();
        Random rnd = new Random();
        long ms = 1614600000000L + (Math.abs(rnd.nextLong()) % ((long) 12 * 24 * 60 * 60 * 1000));
        long ms2 = ms + (Math.abs(rnd.nextLong()) % (3L * 12 * 24 * 60 * 60 * 1000));
        dates.add(new Date(ms));
        dates.add(new Date(ms2));
        return dates;
    }

    public static String passwordAndSN(int amount, String str) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i <= amount; i++) {
            if (Utils.getRand(0, 2) == 1) {
                password.append(str.charAt(new Random().nextInt(str.length() - 1)));
            } else {
                password.append(Utils.getRand(0,10));
            }
        }
        return password.toString();
    }

    public static int lineCounter(String path) {
        BufferedReader reader;
        int lines = 0;
        try {
            reader = new BufferedReader(new FileReader(path));
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (IOException e) {
            System.out.println(FILE_NOT_FOUND_ERROR);
            e.printStackTrace();
        }
        return lines;
    }

    public static ArrayList<String> returnValues(Statement statement, String column, String table) throws SQLException {
        String sql = "SELECT " + column + " FROM " + table;
        ArrayList<String> data = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            data.add(resultSet.getString(1));
        }
        return data;
    }

    public static void generateRandomDB (boolean first_request, int number_of_client, int number_of_worker, int number_of_drug_request) {
        if (first_request) {
            ServicesGenerator.generate();
            DrugGenerator.generate();
            EquipmentGenerator.generate();
        }
        for (int i = 0; i < number_of_client; i++) {
            ClientGenerator.generate();
        }
        for (int i = 0; i < number_of_worker; i++) {
            WorkerGenerator.generate();
        }
        for (int i = 0; i < number_of_drug_request; i++) {
            DrugRequestGenerator.generate();
        }
        for (int i = 0; i < 2; i++) {
            ScheduleOfVisitGenerator.generate();
        }
    }

    public static void writeDataForTimes(String path, List<Long> data) {
        List<Long> list_data = new LinkedList<>();
        data.forEach(it -> list_data.add(0, it));
        try (FileWriter writer = new FileWriter(path, false)) {
            for (Long data_: list_data) {
                writer.write(data_ / 1000000.0 + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeDataForCounts(String path, List<Integer> data) {
        List<Integer> list_data = new LinkedList<>();
        data.forEach(it -> list_data.add(0, it));
        try (FileWriter writer = new FileWriter(path, false)) {
            for (Integer data_: list_data) {
                writer.write(data_ + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
