import java.sql.Connection;

import static transaction.TransactionApplication.startTransactionApplication;
import static vet_clinic_generator.Utils.generateRandomDB;

public class App {

    public static void main(String[] args) {
        generateRandomDB(true, 20, 10, 20);
        startTransactionApplication(300, Connection.TRANSACTION_READ_COMMITTED);
       // startTransactionApplication(300, Connection.TRANSACTION_REPEATABLE_READ);
      //  startTransactionApplication(300, Connection.TRANSACTION_SERIALIZABLE);

   //     startTransactionApplication(800, Connection.TRANSACTION_READ_COMMITTED);
   //   startTransactionApplication(800, Connection.TRANSACTION_REPEATABLE_READ);
     //   startTransactionApplication(800, Connection.TRANSACTION_SERIALIZABLE);
    }
}