package transaction;

import vet_clinic_generator.Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.sql.Connection.*;
import static transaction.FilePaths.*;
import static transaction.TransactionsEnum.*;
import static vet_clinic_generator.ClientGenerator.returnGeneratedClients;
import static vet_clinic_generator.Utils.*;

public class TransactionApplication {

    public static void startTransactionApplication(int num_of_iteration, int isolationLvl) {

        CountDownLatch ready = new CountDownLatch(3);
        CountDownLatch calling = new CountDownLatch(1);
        CountDownLatch completed = new CountDownLatch(3);

        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.setTransactionIsolation(isolationLvl);
            } catch (SQLException e) {
                System.out.println(Utils.DB_CONNECTING_ERROR);
                e.printStackTrace();
            }
        }

        ArrayList<Client> clients = returnGeneratedClients(num_of_iteration);
        TransactionThread selectThread = new TransactionThread(null, num_of_iteration, SELECT, ready,
                calling, completed,connection);
        TransactionThread insertThread = new TransactionThread(clients, num_of_iteration,
                INSERT, ready, calling, completed, connection);
        TransactionThread updateThread = new TransactionThread(null, num_of_iteration, UPDATE, ready,
                calling, completed,connection);

        selectThread.start();
        insertThread.start();
        updateThread.start();

        try {
            ready.await();
            calling.countDown();
            completed.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Long> res1 = insertThread.getTransaction_res();
        List<Long> res2 = selectThread.getTransaction_res();
        List<Long> res3 = updateThread.getTransaction_res();

        switch (num_of_iteration) {
            case 300:
                switch (isolationLvl) {
                    case TRANSACTION_READ_COMMITTED -> createTime300ReadCommittedData(res1, res2, res3);

                    //createCount300ReadCommittedData(res2, res3);
                    case TRANSACTION_REPEATABLE_READ -> createTime300RepeatableReadData(res1, res2, res3);

                    //createCount300RepeatableReadData(res2, res3);
                    case TRANSACTION_SERIALIZABLE -> createTime300SerializableData(res1, res2, res3);

                    //createCount300SerializableData(res2, res3);
                }
                break;
            case 800:
                switch (isolationLvl) {
                    case TRANSACTION_READ_COMMITTED -> createTime800ReadCommittedData(res1, res2, res3);

                    //createCount800ReadCommittedData(res2, res3);
                    case TRANSACTION_REPEATABLE_READ -> createTime800RepeatableReadData(res1, res2, res3);

                    //createCount800RepeatableReadData(res2, res3);
                    case TRANSACTION_SERIALIZABLE ->
                            createTime800SerializableData(res1, res2, res3);

                    //createCount800SerializableData(res2, res3);
                }
                break;
        }

    }

    private static void createTime300ReadCommittedData(List<Long> res1, List<Long> res2, List<Long> res3) {
        writeDataForTimes(TIME_INSERT_300_PATH_READ_COMMITTED, res1);
        writeDataForTimes(TIME_SELECT_300_PATH_READ_COMMITTED, res2);
        writeDataForTimes(TIME_UPDATE_300_PATH_READ_COMMITTED, res3);
    }

    private static void createTime300RepeatableReadData(List<Long> res1, List<Long> res2, List<Long> res3) {
        writeDataForTimes(TIME_INSERT_300_PATH_REPEATABLE_READ, res1);
        writeDataForTimes(TIME_SELECT_300_PATH_REPEATABLE_READ, res2);
        writeDataForTimes(TIME_UPDATE_300_PATH_REPEATABLE_READ, res3);
    }

    private static void createTime300SerializableData(List<Long> res1, List<Long> res2, List<Long> res3) {
        writeDataForTimes(TIME_INSERT_300_PATH_SERIALIZABLE, res1);
        writeDataForTimes(TIME_SELECT_300_PATH_SERIALIZABLE, res2);
        writeDataForTimes(TIME_UPDATE_300_PATH_SERIALIZABLE, res3);
    }

    private static void createCount300ReadCommittedData(List<Long> res2, List<Long> res3) {
        writeDataForCounts(COUNT_SELECT_300_PATH_READ_COMMITTED, res2);
        writeDataForCounts(COUNT_UPDATE_300_PATH_READ_COMMITTED, res3);
    }

    private static void createCount300RepeatableReadData(List<Long> res2, List<Long> res3) {
        writeDataForCounts(COUNT_SELECT_300_PATH_REPEATABLE_READ, res2);
        writeDataForCounts(COUNT_UPDATE_300_PATH_REPEATABLE_READ, res3);
    }

    private static void createCount300SerializableData(List<Long> res2, List<Long> res3) {
        writeDataForCounts(COUNT_SELECT_300_PATH_SERIALIZABLE, res2);
        writeDataForCounts(COUNT_UPDATE_300_PATH_SERIALIZABLE, res3);
    }

    private static void createTime800ReadCommittedData(List<Long> res1, List<Long> res2, List<Long> res3) {
        writeDataForTimes(TIME_INSERT_800_PATH_READ_COMMITTED, res1);
        writeDataForTimes(TIME_SELECT_800_PATH_READ_COMMITTED, res2);
        writeDataForTimes(TIME_UPDATE_800_PATH_READ_COMMITTED, res3);
    }

    private static void createTime800RepeatableReadData(List<Long> res1, List<Long> res2, List<Long> res3) {
        writeDataForTimes(TIME_INSERT_800_PATH_REPEATABLE_READ, res1);
        writeDataForTimes(TIME_SELECT_800_PATH_REPEATABLE_READ, res2);
        writeDataForTimes(TIME_UPDATE_800_PATH_REPEATABLE_READ, res3);
    }

    private static void createTime800SerializableData(List<Long> res1, List<Long> res2, List<Long> res3) {
        writeDataForTimes(TIME_INSERT_800_PATH_SERIALIZABLE, res1);
        writeDataForTimes(TIME_SELECT_800_PATH_SERIALIZABLE, res2);
        writeDataForTimes(TIME_UPDATE_800_PATH_SERIALIZABLE, res3);
    }

    private static void createCount800ReadCommittedData(List<Long> res2, List<Long> res3) {
        writeDataForCounts(COUNT_SELECT_800_PATH_READ_COMMITTED, res2);
        writeDataForCounts(COUNT_UPDATE_800_PATH_READ_COMMITTED, res3);
    }

    private static void createCount800RepeatableReadData(List<Long> res2, List<Long> res3) {
        writeDataForCounts(COUNT_SELECT_800_PATH_REPEATABLE_READ, res2);
        writeDataForCounts(COUNT_UPDATE_800_PATH_REPEATABLE_READ, res3);
    }

    private static void createCount800SerializableData(List<Long> res2, List<Long> res3) {
        writeDataForCounts(COUNT_SELECT_800_PATH_SERIALIZABLE, res2);
        writeDataForCounts(COUNT_UPDATE_800_PATH_SERIALIZABLE, res3);
    }
}
