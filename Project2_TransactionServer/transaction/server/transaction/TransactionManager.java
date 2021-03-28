package transaction.server.transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import transaction.comm.Message;
import transaction.server.TransactionServer;

// The class that handles the incoming clients. This class allows for its
// worker threads to handle connections to clients, giving them transaction
// control while allowing the Manager to continue getting more clients
public class TransactionManager
  {

    private static int transactionCounter = 0;
    private static final ArrayList<Transaction> transactions = new ArrayList<>();

    public TransactionManager()
      {
      }

    public static void runTransaction(Socket client)
      {
        (new TransactionManagerWorker(client)).start();
      }

    public static class TransactionManagerWorker extends Thread
      {
        Socket client = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        Transaction transaction = null;
        int accountNumber = 0;
        int balance = 0;

        boolean keepgoing = true;

        private TransactionManagerWorker(Socket client)
          {
            this.client = client;

            try
              {
                readFromNet = new ObjectInputStream(client.getInputStream());
                writeToNet = new ObjectOutputStream(client.getOutputStream());
              }

            catch (IOException e)
              {
                System.out.println("[TransactionManagerWorker.un] Failed to "
                    + "open object streams");
                e.printStackTrace();
                System.exit(1);
              }
          }

        @Override
        public void run()
          {
            // loop until transaction closed
            while (keepgoing)
              {
                // read message
                try
                  {
                    message = (Message) readFromNet.readObject();
                  }
                catch (IOException | ClassNotFoundException e)
                  {
                    System.out.println("[TransactionManagerWorker.run] Message "
                        + "could not be read from object stream.");
                    System.exit(1);
                  }

                // process message
                switch (message.getType())
                  {
                  // OPEN TRANSACTION
                  // -----------------------------------------------------------
                  case "OPEN":
                    synchronized (transactions)
                      {
                        transaction = new Transaction(transactionCounter++);
                        transactions.add(transaction);
                      }

                    try
                      {
                        writeToNet.writeObject(transaction.getTransactionID());
                      }

                    catch (IOException e)
                      {
                        System.out.println("[TransactionManagerWorker.run] "
                            + "OPEN_TRANSACTION - error when writing transID");
                      }

                    transaction
                        .log("[TransactionManagerWorker.run] OPEN_TRANSACTION #"
                            + transaction.getTransactionID());

                    break;

                  // CLOSE TRANSACTION
                  // -----------------------------------------------------------
                  case "CLOSE":
                    TransactionServer.lockManager.unLock(transaction);
                    transactions.remove(transaction);

                    try
                      {
                        readFromNet.close();
                        writeToNet.close();
                        client.close();
                        keepgoing = false;
                      }

                    catch (IOException e)
                      {
                        System.out.println("[TransactionManagerWorker.run] "
                            + "CLOSE_TRANSACTION - error when closing connection"
                            + " to client");
                      }

                    transaction.log("[TransactionManagerWorker.run] "
                        + "CLOSE_TRANSACTION #"
                        + transaction.getTransactionID());

                    // final printout of all the transaction's log
                    if (TransactionServer.transactionView)
                      {
                        System.out.println(transaction.getLog());
                      }

                    break;

                  // WRITE REQUEST
                  // -----------------------------------------------------------
                  case "WRITE":
                    Object[] content = (Object[]) message.getContent();
                    accountNumber = ((Integer) content[0]);
                    balance = ((Integer) content[1]);
                    transaction.log("[TransactionManagerWorker.run] "
                        + "WRITE_REQUEST >>>>>>>>>>>>> account #"
                        + accountNumber + "new balance $" + balance);

                    balance = TransactionServer.accountManager
                        .write(accountNumber, balance, transaction);

                    try
                      {
                        writeToNet.writeObject((Integer) balance);
                      }

                    catch (IOException e)
                      {
                        System.out.println(
                            "[TransactionManagerWorker.run] WRITE_REQUEST - "
                                + "error when writing to object stream");
                      }

                    transaction.log("[TransactionManagerWorker.run] "
                        + "WRITE_REQUEST <<<<<<<<<<<<< account #"
                        + accountNumber + "new balance $" + balance);

                    break;

                  // READ REQUEST
                  // -----------------------------------------------------------
                  case "READ":
                    Object[] readContent = (Object[]) message.getContent();
                    accountNumber = ((Integer) readContent[0]);
                    transaction.log("[TransactionManagerWorker.run] "
                        + "READ_REQUEST >>>>>>>>>>>>> account #"
                        + accountNumber);

                    balance = TransactionServer.accountManager
                        .read(accountNumber, transaction);

                    try
                      {
                        writeToNet.writeObject((Integer) balance);
                      }

                    catch (IOException e)
                      {
                        System.out.println(
                            "[TransactionManagerWorker.run] WRITE_REQUEST - "
                                + "error when writing to object stream");
                      }

                    transaction.log("[TransactionManagerWorker.run] "
                        + "WRITE_REQUEST <<<<<<<<<<<<< account #"
                        + accountNumber + "new balance $" + balance);

                    break;

                  // DEFAULT
                  // -----------------------------------------------------------
                  default:
                    System.out.println("[TransactionManagerWorker.run] Warning:"
                        + " Message type not implemented");

                  }
              }
          }
      }
  }
