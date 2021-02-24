package chatapp;

import java.util.Scanner;

public class Sender extends Thread
{
    private Scanner userInput;
    private Boolean isConnected;

    public Sender()
    {
        userInput = new Scanner(System.in);
        isConnected = false;
    }

    @Override
    public void run()
    {
        String inputLine;

        while( true )
        {
            inputLine = userInput.nextLine();

            if( inputLine.startsWith("JOIN") )
            {
                if( isConnected )
                {
                    System.out.println("You are already in a chat.");
                    continue;
                }
                else
                {
                    // CONNECT THE USER TO THE GIVEN USER

                    isConnected = true;
                }
            }
            else if( inputLine.startsWith("LEAVE") )
            {
                // DISCONNECT THE USER FROM ALL OTHER USERS

                isConnected = false;
            }
            else if( inputLine.startsWith("SHUTDOWN") )
            {
                // DISCONNECT THE USER FROM ALL AND CLOSE PROGRAM. COULD BE PUT WITH ABOVE CHUNK WITH SLIGHTLY MORE
                // SINCE THIS ONE WILL DO THE EQUIVALENT OF LEAVE WITH ONE EXTRA STEP AFTER
            }
            else if( inputLine.startsWith("NOTE") )
            {
                // SEND THE MESSAGE OUT TO THE PARTICIPANT LIST; REFERENCE TO IT CAN BE AS ChatNode.participantList
            }
            else
            {
                System.out.println("Invalid message type.");
                continue;
            }
        }
    }
}
