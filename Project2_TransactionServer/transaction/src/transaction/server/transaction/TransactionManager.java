package transaction.server.transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import transaction.comm.Message;
import transaction.comm.MessageTypes;
import transaction.server.TransactionServer;

public class TransactionManager implements MessageTypes
  {

    private static int transactionCounter = 0;
    private static final ArrayList<Transaction> transactions = new ArrayList<>();

    public TransactionManager()
      {
      }

    public void runTransaction(Socket client)
      {
        (new TransactionManagerWorker(client)).start();
      }

    public class TransactionManagerWorker extends Thread
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
                  case OPEN_TRANSACTION:
                    synchronized (transactions)
                      {
                        transaction = new Transaction(transactionCounter++);
                        transaction.add(transaction);
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
                  case CLOSE_TRANSACTION:
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
                  case WRITE_REQUEST:
                    Object[] content = (Object[]) message.getContent();
                    accountNumber = ((Integer) content[0]);
                    balance = ((Integer) content[1]);
                    transaction.log("[TransactionManagerWorker.run] "
                        + "WRITE_REQUEST >>>>>>>>>>>>> account #"
                        + accountNumber + "new balance $" + balance);

                    balance = TransactionServer.accountManager
                        .write(accountNumber, transaction, balance);

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
