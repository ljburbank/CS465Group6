package transaction.client;

import java.util.Scanner;

// The primary class of the client. This allow the user to call on 4 high level 
// operations for transactions: open, close, read, write. The user can call
// these 4 operations and it will have an effect in the server through the proxy
public class TransactionClient
{
    public static void main(String[] args)
    {
        boolean isRunning = true;
        boolean inTransaction = false;
        TransactionServerProxy connection = new TransactionServerProxy();

        Scanner in = new Scanner(System.in);

        while( isRunning )
        {
            // get the user's next input
            System.out.print("Next input: ");
            String userInput = in.nextLine();

            // check if the input is a valid operation
            if( userInput.equals("OPEN TRANSACTION") )
            {
                if( inTransaction )
                {
                    System.out.println("You are already in a transaction.");
                }
                else
                {
                    connection.openTransaction();
                    inTransaction = true;
                }
            }
            else if( userInput.equals("CLOSE TRANSACTION") )
            {
                if( !inTransaction )
                {
                    System.out.println("You are not currently in a transaction.");
                }
                else
                {
                    connection.closeTransaction();
                    inTransaction = false;
                }
            }
            else if( userInput.startsWith("READ") )
            {
                if( !inTransaction )
                {
                    System.out.println("You are not currently in a transaction.");
                }
                else
                {
                    String[] splitInput = userInput.split(" ");

                    try
                    {
                        connection.read(Integer.parseInt(splitInput[1]));
                    }
                    catch (Exception err)
                    {
                        System.out.println("Invalid read format. Format as READ (accountNumber)");
                    }
                }
            }
            else if( userInput.startsWith("WRITE") )
            {
                if( !inTransaction )
                {
                    System.out.println("You are not currently in a transaction.");
                }
                else
                {
                    String[] splitInput = userInput.split(" ");

                    try
                    {
                        connection.write(Integer.parseInt(splitInput[1]), Integer.parseInt(splitInput[2]));
                    }
                    catch (Exception err)
                    {
                        System.out.println("Invalid write format. Format as WRITE (accountNumber) (newBalance)");
                    }
                }
            }
            else if( userInput.equals("EXIT") )
            {
                isRunning = false;
            }
            else
            {
                System.out.println("That is not a valid operation.");
            }
        }

        // close the connection and the scanner
        connection.closeConnection();
        in.close();

        System.out.println("System shutting down.");
    }
}