import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class EchoServer
{
    public static void main(String[] args)
    {
	//hardcoded port number on an unused port
        int portNumber = 4444;

	//try opening a ServerSocket in a try-with-resources
        try
        (
            ServerSocket server = new ServerSocket(portNumber);
        )
            {
		//keep looping to accept new clients constantly
                while (true)
                {
		    //accept a client socket
                    Socket client = server.accept();
                    System.out.println("New Connection");

		    //create an EchoThread to start handling the client socket
                    EchoThread handler = new EchoThread(client);

		    //start running the EchoThread to deal with the Socket so the loop can keep accepting
                    new Thread(handler).start();
                }
            } catch(IOException err)
            {
                err.printStackTrace();
            }
    }

    private static class EchoThread implements Runnable
    {
	//class variables
        private Socket client;
    
    	/**
     	* EchoThread initializes thread object with given socket connection.
     	* @param client - socket connection to be handled by thread.
     	*/
    	
	public EchoThread( Socket client )
        {
            this.client = client;
        }
    
    
    
    /**
     * Run function opens up data IO steams with client then listens.
     * Reads characters from client one at a time. Processes each character as follows:
     *  - ignores characters that are not letters.
     *  - looks for the sequence "quit", terminates connection if found.
     *  - all characters that are letters are echoed back to the client.
     */
    @Override
    public void run() 
        {
         //initialize function / variables
         DataInputStream fromClient;  //data stream from client
         DataOutputStream toClient;   //data stream to client
         char charFromClient;         //character received from client via input stream
         char workingChar;            //copy of clientData for lossless processing
         char prevChar = ' ';         //default init value, serves no purpose
         Boolean listen = true;       //control boolean for listening while loop
         
         try  //IOException possible, must be caught.
             {
              //get IO streams
              fromClient = new DataInputStream( client.getInputStream() );
              toClient = new DataOutputStream( client.getOutputStream() );
             
              //while loop to listen to client
              while( listen )
                  {
                   //read input from client
                   charFromClient = (char) fromClient.readByte();
                   System.out.println(charFromClient);
                  
                   //process char from user: check if letter, check for "quit"
                   //check if char is letter
                   if( Character.isLetter( charFromClient ) )
                       {
                        //if letter, make lowercase, working copy
                        workingChar = Character.toLowerCase( charFromClient );
                        
                        //if letter is q
                        if( workingChar == 'q' )
                            {
                             //note q seen
                             prevChar = 'q';
                             
                            }
                        
                        //else if q is seen, check for u
                        else if ( prevChar == 'q' && workingChar == 'u')
                            {
                             //note u seen
                            prevChar = 'u';
                            }
                        
                        //else if u seen, check for i
                        else if( prevChar == 'u' && workingChar == 'i' )
                            {
                             //note i seen
                             prevChar = 'i';
                            }
                        
                        //else if i seen, check for t
                        else if( prevChar == 'i' && workingChar == 't' )
                            {
                             //note t seen
                             prevChar = 't';
                            
                            }
                        
                        //else quit sequence broken, clear prevChar.
                        else
                            {
                             prevChar = ' ';
                            }
                        
                        //echo character to client
                        toClient.writeByte( clientData );

                        //check for 'quit' seen
                        if( prevChar == 't' )
                            {
                             listen = false; //exit server while loop
                            }
                       }
                       //end character processing
                   
                  }
                  //end server while loop
              client.close();
             } 
        
         //catch block for io exceptions: print exception, continue.
         catch (IOException e)
             {
              //print stack trace
              e.printStackTrace();
            
             }
        
        
         }

    }
}