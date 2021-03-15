package chatapp;

import java.io.IOException;
import java.net.ServerSocket;


// class declaration, Reciever has its own thread.
public class Receiver extends Thread
{
    static ServerSocket receiverSocket = null;

    ChatNode thisNode;
    // init constructor
    // allows recieverSocket to be set to a new serverSocket
    public Receiver( ChatNode thisNode )

    {
        this.thisNode = thisNode;
        try
        {
            receiverSocket = new ServerSocket( thisNode.myInfo.getPort() );
        }
        catch ( IOException err )
        {
            err.printStackTrace();
        }
    }

    // thread runs
    @Override
    public void run()
    {
        while( true )
        {
            try
            {
                ( new ReceiverWorker( receiverSocket.accept(), thisNode ) ).start();
            }
            catch ( IOException err )
            {
                err.printStackTrace();
            }
        }
    }
}
