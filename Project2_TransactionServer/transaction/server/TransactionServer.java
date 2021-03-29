package transaction.server;

import transaction.server.lock.LockManager;
import transaction.server.account.AccountManager;
import transaction.server.transaction.TransactionManager;
import java.net.ServerSocket;
import java.net.Socket;

// The primary class of the server. This will run the server loop and 
// initialize all of the managers as singleton Objects
public class TransactionServer
{
    public static LockManager lockManager;
    public static AccountManager accountManager;
    public static TransactionManager transactionManager;

    public static boolean transactionView = true;

    ServerSocket receiverSocket = null;

    public TransactionServer()
    {
        Boolean useLocks = true;  //determines whether race conditions are controlled by locks; true for use locks
        lockManager = new LockManager(useLocks);
        accountManager = new AccountManager(10, 10);
        transactionManager = new TransactionManager();
    }

    // Start the loop of listening for client requests. When one is found
    // pass it on to the TransactionManager and keep waiting for more
    private void startListening()
    {
        // Start listening on a given port
        try
        {
            receiverSocket = new ServerSocket(5555);        // CHANGE TO WORK WITH CONFIG
        }
        catch (Exception err)
        {
            System.out.println("Error initializing the server socket.");
        }

        // Begin the server loop
        while( true )
        {
            System.out.println("Listening for connection");
            
            try
            {
                Socket acceptedSocket = receiverSocket.accept();
                TransactionManager.runTransaction( acceptedSocket );
            }
            catch (Exception err)
            {
                System.out.println("Error accepting a connection.");
            }
        }
    }

    // Initializes a TransactionServer object and starts listening for connnections
    public static void main(String[] args)
    {
        (new TransactionServer()).startListening();
    }

}
