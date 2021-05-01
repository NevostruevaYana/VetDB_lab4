package transaction;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static transaction.Transactions.*;

public class TransactionThread extends Thread{

    private final int num_of_iterations;
    private final TransactionsEnum curr_transaction;
    private final CountDownLatch ready;
    private final CountDownLatch calling;
    private final CountDownLatch completed;
    private final ArrayList<Client> clients;
    private final Connection connection;

    public List<Long> getTransaction_res() {
        return transaction_res;
    }

    public List<Integer> getTransaction_res_2() {
        return transaction_res_2;
    }

    public void setTransaction_res(List<Long> transaction_res) {
        this.transaction_res = transaction_res;
    }

    public void setTransaction_res_2(List<Integer> transaction_res_2) {
        this.transaction_res_2 = transaction_res_2;
    }

    private List<Long> transaction_res;
    private List<Integer> transaction_res_2;

    public TransactionThread(ArrayList<Client> clients, int num_of_iterations, TransactionsEnum curr_transaction,
                             CountDownLatch ready, CountDownLatch calling, CountDownLatch completed,
                             Connection connection) {
        this.num_of_iterations = num_of_iterations;
        this.curr_transaction = curr_transaction;
        this.ready = ready;
        this.calling = calling;
        this.completed = completed;
        this.clients = clients;
        this.connection = connection;
    }

    public void run() {
        ready.countDown();
        try {
            calling.await();
            switch (curr_transaction) {
                case INSERT:
                    transaction_res = insertClientForTransaction(clients, connection);
                case SELECT:
                    transaction_res = selectClientForTransaction(num_of_iterations, connection);
                    //transaction_res_2 = selectClientForTransaction2(num_of_iterations, connection);
                case UPDATE:
                    transaction_res = updateClientForTransaction(num_of_iterations, connection);
                    //transaction_res_2 = updateClientForTransaction2(num_of_iterations, connection);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            completed.countDown();
        }
    }
}
