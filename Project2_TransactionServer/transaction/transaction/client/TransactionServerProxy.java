package transaction.client;

import transaction.comm.Message;
import java.net.Socket;
import java.io.*;

// The client side class that connects directly to the server. This class is
// called and used by the TransactionClient to connect to the transaction
// server through the high level operations
public class TransactionServerProxy
{
    String serverIP;
    int serverPort;

    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;

    public TransactionServerProxy()
    {
        serverIP = "localhost";
        serverPort = 5555;      // CHANGE TO WORK WITH A CONFIG FILE

        // create the connection to TransactionServer
        try
        {
            socket = new Socket(serverIP, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("Connected");
        }
        catch (Exception err)
        {
            System.out.println("Proxy could not connect to the transaction server.");
        }
    }

    // Send an open transaction message to TransactionServer
    public void openTransaction()
    {
        Message outMsg;

        String type = "OPEN";
        Object content[] = new Object[]{};
        outMsg = new Message(type, content);

        try
        {
            out.writeObject(outMsg);
            in.readObject();
        }
        catch (Exception err)
        {
            System.out.println("Could not send message object.");
        }
    }

    // Send a close transaction message to TransactionServer
    public void closeTransaction()
    {
        Message outMsg;

        String type = "CLOSE";
        Object content[] = new Object[]{};
        outMsg = new Message(type, content);

        try
        {
            out.writeObject(outMsg);
            in.readObject();
        }
        catch (Exception err)
        {
            System.out.println("Could not send message object.");
        }
    }

    // Send a read message and the account id to the TransactionServer
    public int read( int accountId )
    {
        Message outMsg;
        int balance = 0;

        String type = "READ";
        Object content[] = new Object[]{(Object) accountId};
        outMsg = new Message(type, content);

        try
        {
            out.writeObject(outMsg);
            balance = (Integer) in.readObject();
        }
        catch (Exception err)
        {
            System.out.println("Could not send message object.");
        }

        return balance;   // actually return the in
    }

    // Send a write message, account id, and amount to the TransactionServer
    public void write( int accountId, int amount )
    {
        Message outMsg;

        String type = "WRITE";
        Object content[] = new Object[]{accountId, amount};
        outMsg = new Message(type, content);

        try
        {
            out.writeObject(outMsg);
            in.readObject();
        }
        catch (Exception err)
        {
            System.out.println("Could not send message object.");
        }
    }

    // Close the connection to TransactionServer
    public void closeConnection()
    {
        try
        {
            socket.close();
        }
        catch (Exception err)
        {
            System.out.println("Error closing the connection.");
        }
    }
}
