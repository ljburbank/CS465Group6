package chatapp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Sender extends Thread
  {
    private Scanner userInput;
    private Boolean isConnected;
    private ChatNode thisNode;

    public Sender( ChatNode thisNode)
      {
        userInput = new Scanner(System.in);
        isConnected = false;
        this.thisNode = thisNode;
      }

    @Override
    public void run()
    {
        //init function / variables
        Boolean running = true;
        String messageType, IP, message;
        int port;
        int index;
        ObjectOutputStream out;
        ObjectInputStream input;
        Socket socket;
        UserMessage outMessage;
        UserMessage inMessage;
        
        
        while( running )
        {
            //prompt user for message type
            messageType = userInput.next();

            //if message type is join
            if( messageType.equals( "JOIN" ) )
            {
              //ask for target ip
              System.out.print("Input an IP to join: ");
              IP = userInput.next();
              
              //ask for target port
              System.out.print("Input a port number: ");
              port = Integer.parseInt(userInput.next());
              
              
                if( isConnected )
                {
                    System.out.println("You are already in a chat.");
                    continue;
                }
                
                else
                {
                    // CONNECT THE USER TO THE GIVEN USER
                    
                    //pack message
                    message = messageType;
                    outMessage = new UserMessage( thisNode.myInfo, message);
                    
                    try
                      {
                        socket = new Socket(IP, port);
                        System.out.println("Connected");
                        
                        // send myInfo to socket
                        out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject( outMessage );
                        
                        // get participantList
                        input = new ObjectInputStream(socket.getInputStream());
                        try
                          {
                            inMessage = (UserMessage) input.readObject();
                            for( index = 0; index < inMessage.participantList.size(); index++)
                                {
                                 thisNode.addParticipant(inMessage.participantList.get( index ));
                                }
                            thisNode.participantList = inMessage.participantList;
                            System.out.println(thisNode.participantList);
                            
                          } 
                        
                        catch (ClassNotFoundException e)
                          {
                            System.out.println("Failed to migrate participants list");
                            e.printStackTrace();
                          }
                        socket.close();
                      } 
                    
                    catch (IOException err)
                      {
                        System.out.println("Socket connection failed");
                        err.printStackTrace();
                      }
                    
                    isConnected = true;
                }
            }
            
            
            else if( messageType.equals( "LEAVE" ) )
            {
                // DISCONNECT THE USER FROM ALL OTHER USERS
                for( ChatNode.NodeInfo participants : thisNode.participantList)
                  {
                    try
                      {
                        socket = new Socket(participants.IP, participants.Port);
                        UserMessage leaveMsg = new UserMessage(thisNode.myInfo, "LEAVE");
                        out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(leaveMsg);
                        
                        input = new ObjectInputStream(socket.getInputStream());
                        input.readObject();
                        socket.close();
                      } 
                    
                    catch (IOException | ClassNotFoundException e)
                      {
                        System.out.println("Socket connection failed");
                        e.printStackTrace();
                      }
                  }
                System.out.println("You have left the chat");
                isConnected = false;
            }
            
            
            else if( messageType.equals( "SHUTDOWN" ) )
            {
                // DISCONNECT THE USER FROM ALL AND CLOSE PROGRAM. COULD BE PUT WITH ABOVE CHUNK WITH SLIGHTLY MORE
                // SINCE THIS ONE WILL DO THE EQUIVALENT OF LEAVE WITH ONE EXTRA STEP AFTER
              for( ChatNode.NodeInfo participants : thisNode.participantList)
                {
                  try
                    {
                      socket = new Socket(participants.IP, participants.Port);
                      UserMessage leaveMsg = new UserMessage(thisNode.myInfo, "SHUTDOWN");
                      out = new ObjectOutputStream(socket.getOutputStream());
                      out.writeObject(leaveMsg);
                      
                      input = new ObjectInputStream(socket.getInputStream());
                      input.readObject();
                      socket.close();
                    } 
                  
                  catch (IOException | ClassNotFoundException e)
                    {
                      System.out.println("Socket connection failed");
                      e.printStackTrace();
                    }
                }
              System.out.println("You have shutdown the chat");
              running = false;
              System.exit(0);
            }
            
            
            // NOTE "message" ( we won't need name because we included in NodeInfo
            else if( messageType.equals( "NOTE" ) )
            {
              // SEND THE MESSAGE OUT TO THE PARTICIPANT LIST; REFERENCE TO IT CAN BE AS ChatNode.participantList
              System.out.print("Input a message to send: ");
              message = userInput.next();
              for( ChatNode.NodeInfo participants : thisNode.participantList)
                {
                  try
                    {
                      socket = new Socket(participants.IP, participants.Port);
                      UserMessage leaveMsg = new UserMessage(thisNode.myInfo, message);
                      out = new ObjectOutputStream(socket.getOutputStream());
                      out.writeObject(leaveMsg);
                      
                      input = new ObjectInputStream(socket.getInputStream());
                      input.readObject();
                      socket.close();
                    } 
                  
                  catch (IOException | ClassNotFoundException e)
                    {
                      System.out.println("Socket connection failed");
                      e.printStackTrace();
                    }
                }
            }
            
            else
            {
                System.out.println("\nInvalid message type.");
                continue;
            }
        }
    }
  }