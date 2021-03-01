package chatapp;

import java.io.Serializable;
import java.util.LinkedList;

public class ChatNode implements Serializable
  {


    public class NodeInfo implements Serializable
      {
        private static final long serialVersionUID = 2L;
        String IP;
        String Name;
        int Port;

        public NodeInfo(String IP, String inName, int inPort)
          {
            this.IP = IP;
            Name = inName;
            Port = inPort;
          }
        
        public int getPort()
          {
            return this.Port;  
          }
        
        public String toString()
            {
             return IP + "," + Name + "," + String.valueOf( Port );
            }
      }

    static Receiver receiver = null;
    static Sender sender = null;

    public NodeInfo myInfo;
    public LinkedList<NodeInfo> participantList;
    private static final long serialVersionUID = 3L;

    public ChatNode(String inIp, String inName, int inPort)
      {
        myInfo = new NodeInfo(inIp, inName, inPort);
        participantList = new LinkedList<NodeInfo>( );
        participantList.add( this.myInfo );
     
      }
    
    public void addParticipant( NodeInfo newNode )
        {
         //initialize function
         int index;
         NodeInfo workingInfo;
         int workingPort;
         String workingIP;
         int newPort = newNode.Port;
         String newIP = newNode.IP;
         Boolean userExists = false;
         
         //iterate through participants list
         for( index = 0; index < participantList.size(); index++ )
             {
              //unpack participant list
              workingInfo = this.participantList.get(index);
              workingPort = workingInfo.Port;
              workingIP = workingInfo.IP;
              
              //check if node already in list
              if( newPort == workingPort && newIP.equals(workingIP ) )
                  {
                   userExists = true;
                  }
              
             }
         
         //if user not found
         if( userExists == false )
             {
              participantList.add( newNode );
             }
         
        }
    
    public void run()
      {
        System.out.println(",,,,,,,,,,,,,,,,");
        System.out.println("Chat Application");
        System.out.println("````````````````");
        System.out.println("");
        
        System.out.println("IP: " + this.myInfo.IP);
        System.out.println("Name: " + this.myInfo.Name);
        System.out.println("Port: " + this.myInfo.Port);
        System.out.println("");
        
        (receiver = new Receiver( this )).start();
        (sender = new Sender( this )).start();
      }

    public static void main(String[] args)
      {
        (new ChatNode(args[0], args[1], Integer.parseInt(args[2]))).run();
        
      }
    
    
  }

