package transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static vet_clinic_generator.Utils.SERIALIZABLE_ERROR;

public class Transactions {

    public static List<Long> insertClientForTransaction(ArrayList<Client> clients, Connection connection) {
        List<Long> insert_time_ = new LinkedList<>();
        boolean flag = false;
        String sql = "INSERT INTO client (full_name, address, phone, regular) " +
                "VALUES ('%s','%s','%s','%b')";
        for (Client client : clients) {
            long start = System.nanoTime();
            do {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(String.format(sql, client.getFull_name(),
                            client.getAddress(), client.getPhone(), false));
                    flag =  false;
                } catch (SQLException e) {
                    if (e.getSQLState().equals(SERIALIZABLE_ERROR))
                        flag = true;
                }
            } while (flag);
            insert_time_.add(0, System.nanoTime() - start);
        }
        closeConnection(connection);
        return insert_time_;
    }

    public static List<Long> selectClientForTransaction(int num_of_iterations, Connection connection) {
        List<Long> select_time = new LinkedList<>();
        boolean flag = false;
        long start = System.nanoTime();
        for (int i = 0; i < num_of_iterations; i++) {
            do {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("SELECT * FROM client WHERE regular = true");
                    flag = false;
                } catch (SQLException e) {
                    if (e.getSQLState().equals(SERIALIZABLE_ERROR))
                        flag = true;
                }
            } while (flag);
            select_time.add(0, System.nanoTime() - start);
        }
        closeConnection(connection);
        return select_time;
    }

    public static List<Long> updateClientForTransaction(int num_of_iterations, Connection connection) {
        List<Long> update_time = new LinkedList<>();
        boolean flag = false;
        long start = System.nanoTime();
        for (int i = 0; i < num_of_iterations; i++) {
            do {
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("UPDATE client SET regular = true WHERE phone LIKE '%7'");
                    flag = false;
                } catch (SQLException e) {
                    if (e.getSQLState().equals(SERIALIZABLE_ERROR))
                        flag = true;
                }
            } while (flag);
            update_time.add(0, System.nanoTime() - start);
        }
        closeConnection(connection);
        return update_time;
    }

    private static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
