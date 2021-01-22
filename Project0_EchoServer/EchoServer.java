import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class EchoServer
{
    public static void main(String[] args)
    {
        int portNumber = 4444;

        try
        (
            ServerSocket server = new ServerSocket(portNumber);
        )
            {
                while (true)
                {
                    Socket client = server.accept();
                    System.out.println("New Connection");
                    EchoThread handler = new EchoThread(client);
                    new Thread(handler).start();
                }
            } catch(IOException err)
            {
                err.printStackTrace();
            }
    }

    private static class EchoThread implements Runnable
    {
        Socket clientSocket;

        EchoThread(Socket client)
        {
            clientSocket = client;
        }

        @Override
        public void run()
        {
            try
            (
                PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            )   {
                    String inputLine, outputLine;

                    while((inputLine = fromClient.readLine()) != null)
                    {
                        outputLine = processInput(inputLine);
                        toClient.println(outputLine);
                        if(outputLine.toLowerCase().contains("quit"))
                        {
                            System.out.println("Connection Lost");
                            break;
                        }
                    }
                } catch(IOException err)
                {
                    err.printStackTrace();
                }
        }

        private String processInput(String inputLine)
        {
            String resultLine = "";
            for(int i = 0; i < inputLine.length(); i++)
            {
                if( (inputLine.charAt(i) >= 'a' && inputLine.charAt(i) <= 'z') ||
                    (inputLine.charAt(i) >= 'A' && inputLine.charAt(i) <= 'Z') )
                {
                    resultLine += inputLine.charAt(i);
                }
            }
            return resultLine;
        }

    }
}