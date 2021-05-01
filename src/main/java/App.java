import java.sql.Connection;

import static transaction.TransactionApp.startTransactionApp;
import static vet_clinic_generator.Utils.deleteAllInfo;
import static vet_clinic_generator.Utils.generateRandomDB;

public class App {

    public static void main(String[] args) {
        deleteAllInfo();
        generateRandomDB(true, 20, 10, 20);
        //startTransactionApp(3000, Connection.TRANSACTION_READ_COMMITTED);
        //startTransactionApp(800, Connection.TRANSACTION_REPEATABLE_READ);
        startTransactionApp(800, Connection.TRANSACTION_SERIALIZABLE);

        //startTransactionApp(800, Connection.TRANSACTION_READ_COMMITTED);
        //startTransactionApp(800, Connection.TRANSACTION_REPEATABLE_READ);
        //startTransactionApp(800, Connection.TRANSACTION_SERIALIZABLE);
    }
}