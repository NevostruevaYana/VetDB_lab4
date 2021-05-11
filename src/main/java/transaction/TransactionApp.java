package transaction;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.sql.Connection.*;
import static transaction.FilePaths.*;
import static transaction.TransactionsEnum.*;
import static vet_clinic_generator.ClientGenerator.returnGeneratedClients;
import static vet_clinic_generator.Utils.getConnection;
import static vet_clinic_generator.Utils.writeDataForTimes;

public class TransactionApp {

    public static void startTransactionApp(int num_of_iteration, int isolationLvl) {

        CountDownLatch ready = new CountDownLatch(3);
        CountDownLatch calling = new CountDownLatch(1);
        CountDownLatch completed = new CountDownLatch(3);

        Connection connection_for_insert = getConnection(isolationLvl);
        Connection connection_for_select = getConnection(isolationLvl);
        Connection connection_for_update = getConnection(isolationLvl);

        ArrayList<Client> clients = returnGeneratedClients(num_of_iteration);
        TransactionThread selectThread = new TransactionThread(null, num_of_iteration, SELECT, ready,
                calling, completed, connection_for_select);
        TransactionThread insertThread = new TransactionThread(clients, num_of_iteration,
                INSERT, ready, calling, completed, connection_for_insert);
        TransactionThread updateThread = new TransactionThread(null, num_of_iteration, UPDATE, ready,
                calling, completed, connection_for_update);

        insertThread.start();
        selectThread.start();
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


        switch (isolationLvl) {
            case TRANSACTION_READ_COMMITTED -> createTimeReadCommittedData(res1, res2, res3);
            case TRANSACTION_REPEATABLE_READ -> createTimeRepeatableReadData(res1, res2, res3);
            case TRANSACTION_SERIALIZABLE -> createTimeSerializableData(res1, res2, res3);
        }

    }

    private static void createTimeReadCommittedData(List<Long> res1, List<Long> res2, List<Long> res3) {
        writeDataForTimes(TIME_INSERT_READ_COMMITTED, res1);
        writeDataForTimes(TIME_SELECT_READ_COMMITTED, res2);
        writeDataForTimes(TIME_UPDATE_READ_COMMITTED, res3);
    }

    private static void createTimeRepeatableReadData(List<Long> res1, List<Long> res2, List<Long> res3) {
        writeDataForTimes(TIME_INSERT_REPEATABLE_READ, res1);
        writeDataForTimes(TIME_SELECT_REPEATABLE_READ, res2);
        writeDataForTimes(TIME_UPDATE_REPEATABLE_READ, res3);
    }

    private static void createTimeSerializableData(List<Long> res1, List<Long> res2, List<Long> res3) {
        writeDataForTimes(TIME_INSERT_SERIALIZABLE, res1);
        writeDataForTimes(TIME_SELECT_SERIALIZABLE, res2);
        writeDataForTimes(TIME_UPDATE_SERIALIZABLE, res3);
    }

}
