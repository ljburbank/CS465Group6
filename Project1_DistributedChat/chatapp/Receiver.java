package chatapp;

import java.io.IOException;
import java.io.Serializable;

public class Receiver extends Thread implements Serializable
{
    private String ip, name;
    private int port;

    public Receiver( ChatNode node )
    {
        ip = this.ip;
        name = this.name;
        port = this.port;
    }

    @Override
    public void run()
    {
        while( true )
        {
            try
            {
                (new ReceiverWorker(receiverSocket.accept())).start();
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
    }
}
