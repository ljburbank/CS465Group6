package chatapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import chatapp.ChatNode.NodeInfo;

public class ReceiverWorker extends Thread
{
    Socket socket;
    ChatNode thisNode;
    public ReceiverWorker( Socket connection, ChatNode thisNode )
    {
        // MAKE REFERENCE TO THE CONNECTION
        this.socket = connection;
        this.thisNode = thisNode;
    }

    @Override
    public void run()
    {
        ObjectOutputStream outStream = null;
        ObjectInputStream inStream;
        UserMessage inMessage = null;
        UserMessage outMessage = null;
        int senderPort;
        String senderIP;
        String messageContent;
        ChatNode.NodeInfo workingInfo = null;
        LinkedList<NodeInfo> newParticipants;
        int index;
        
        // SIMILAR TO SENDER, EXCEPT THE STRING TO PROCESS IS INPUT FROM THE CONNECTION
        // THEN LOOP IT THRU IF STATEMENTS CHECKING FOR WHAT KIND OF MESSAGE IT IS LIKE IN SENDER
        // THE ITEM BEING SENT WILL BE A UserMessage OBJECT
        
        //Open IO streams, receive message object
        try
            {
             outStream = new ObjectOutputStream( socket.getOutputStream());
             inStream = new ObjectInputStream( socket.getInputStream() );
             
             inMessage = (UserMessage) inStream.readObject();
            } 
        
        catch (IOException | ClassNotFoundException e)
            {
             e.printStackTrace();
             System.exit(1);
            }
        
        //get message from object
        messageContent = inMessage.message;
        System.out.println("Message received from " + inMessage.senderInfo.Name + ": " + messageContent);
        
        //if message is JOIN
        if( messageContent.equals("JOIN"))
            {       
             //write new participant to list
             thisNode.addParticipant( inMessage.senderInfo );
            
             //pack message
             outMessage = new UserMessage( thisNode.myInfo, "" );
             outMessage.participantList = thisNode.participantList;
             
             //send message object
             try
                 {
                  outStream.writeObject( outMessage );
                 }
             catch (IOException e)
                 {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                 }
             
       
             //then, send updated participants list to all participants
             outMessage.message = "PARTICIPANTS";
             for( index = 0; index < thisNode.participantList.size(); index++ )
                 {
                  //get info, unpack
                  workingInfo = thisNode.participantList.get(index);
                  senderPort = workingInfo.Port;
                  senderIP = workingInfo.IP;
                  
                  if( senderPort != thisNode.myInfo.Port )
                    //open socket
                    try
                      {
                       socket = new Socket( senderIP, senderPort );
                       outStream = new ObjectOutputStream(socket.getOutputStream());
                       outStream.writeObject( outMessage );
                       
                       inStream = new ObjectInputStream( socket.getInputStream() );
                       inStream.readObject();
                       socket.close();
                      }
                    catch (IOException | ClassNotFoundException e)
                      {
                       e.printStackTrace();
                      }
                  
                 }
 
            }
       
        
        //if message is LEAVE
        else if( messageContent.equals("LEAVE") || messageContent.equals("SHUTDOWN") )
            {
             //unpack leaving node's info
             senderPort = inMessage.senderInfo.Port;
             senderIP = inMessage.senderInfo.IP;
             
             //echo message to ack
             try
                {
                    outStream.writeObject( inMessage );
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
             
             //update participants list
                 //iterate through linked list
                 for( index = 0; index < thisNode.participantList.size(); index++ )
                     { 
                      workingInfo = thisNode.participantList.get(index);
                      
                      //check if node to remove
                      if( workingInfo.IP.equals( senderIP ) && workingInfo.Port == senderPort )
                          {
                           thisNode.participantList.remove( index );
                          }
                     }
                 

             
            }
        
        
        //if message is PARTICIPANTS
        else if(messageContent.equals("PARTICIPANTS") )
            {
             //unpack participants
             newParticipants = inMessage.participantList;
             
             //iterate through received participants and add
             for( index = 0; index < newParticipants.size(); index++ )
                 {
                  workingInfo = newParticipants.get(index);
                  thisNode.addParticipant( workingInfo );
                  
                 }
             try
            {
                outStream.writeObject(inMessage);
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            }
        //else assume message is just a note
        else
            {
             //echo message as acknowledge
               try
              {
                  outStream.writeObject(inMessage);
              } catch (IOException e)
              {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
            }
        // no action.
        
    }
}
