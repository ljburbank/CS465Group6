package chatapp;

import java.net.Socket;

public class ReceiverWorker extends Thread
{
    public ReceiverWorker( Socket connection )
    {
        // MAKE REFERENCE TO THE CONNECTION
    }

    @Override
    public void run()
    {
        // SIMILAR TO SENDER, EXCEPT THE STRING TO PROCESS IS INPUT FROM THE CONNECTION
        // THEN LOOP IT THRU IF STATEMENTS CHECKING FOR WHAT KIND OF MESSAGE IT IS LIKE IN SENDER
        // THE ITEM BEING SENT WILL BE A UserMessage OBJECT
    }
}
