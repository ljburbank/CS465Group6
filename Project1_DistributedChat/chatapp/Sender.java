package chatapp;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

import chatapp.ChatNode.NodeInfo;

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
        Boolean running = true;
        String messageType, IP, message;
        int port;
        ObjectOutputStream out;
        ObjectInputStream input;
        Socket socket;
        
        while( running )
        {
            messageType = userInput.next();


            if( messageType.equals( "JOIN" ) )
            {
              IP = userInput.next();
              port = Integer.parseInt(userInput.next());
              
                if( isConnected )
                {
                    System.out.println("You are already in a chat.");
                    continue;
                }
                
                else
                {
                    // CONNECT THE USER TO THE GIVEN USER
                    try
                      {
                        socket = new Socket(IP, port);
                        System.out.println("Connected");
                        
                        // send myInfo to socket
                        out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(ChatNode.myInfo);
                        
                        // get participantList
                        input = new ObjectInputStream(socket.getInputStream());
                        try
                          {
                            ChatNode.participantList = (LinkedList<NodeInfo>) input.readObject();
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
                for( ChatNode.NodeInfo participants : ChatNode.participantList)
                  {
                    try
                      {
                        socket = new Socket(participants.IP, participants.Port);
                        UserMessage leaveMsg = new UserMessage(ChatNode.myInfo, "LEAVE");
                        out = new ObjectOutputStream(socket.getOutputStream());
                        out.writeObject(leaveMsg);
                        socket.close();
                      } 
                    
                    catch (IOException e)
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
              for( ChatNode.NodeInfo participants : ChatNode.participantList)
                {
                  try
                    {
                      socket = new Socket(participants.IP, participants.Port);
                      UserMessage leaveMsg = new UserMessage(ChatNode.myInfo, "SHUTDOWN");
                      out = new ObjectOutputStream(socket.getOutputStream());
                      out.writeObject(leaveMsg);
                      socket.close();
                    } 
                  
                  catch (IOException e)
                    {
                      System.out.println("Socket connection failed");
                      e.printStackTrace();
                    }
                }
              System.out.println("You have shutdown the chat");
              running = false;
            }
            
            // NOTE "message" ( we won't need name because we included in NodeInfo
            else if( messageType.equals( "NOTE" ) )
            {
              // SEND THE MESSAGE OUT TO THE PARTICIPANT LIST; REFERENCE TO IT CAN BE AS ChatNode.participantList
              message = userInput.next();
              for( ChatNode.NodeInfo participants : ChatNode.participantList)
                {
                  try
                    {
                      socket = new Socket(participants.IP, participants.Port);
                      UserMessage leaveMsg = new UserMessage(ChatNode.myInfo, message);
                      out = new ObjectOutputStream(socket.getOutputStream());
                      out.writeObject(leaveMsg);
                      socket.close();
                    } 
                  
                  catch (IOException e)
                    {
                      System.out.println("Socket connection failed");
                      e.printStackTrace();
                    }
                }
              System.out.println("You have left the chat");
            }
            
            else
            {
                System.out.println("Invalid message type.");
                continue;
            }
        }
    }
  }