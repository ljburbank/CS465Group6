package chatapp;

import java.io.IOException;
import java.net.ServerSocket;

public class Receiver extends Thread
{
    static ServerSocket receiverSocket = null;
    ChatNode thisNode;
    
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
