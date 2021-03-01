package chatapp;

import java.util.LinkedList;
/* 
   ChatNode class 
   handles chat node object
   creates chat thread and calls in maib
*/
public class ChatNode
  {

    /*
      Nested class containing 
      nodeinfo.
    */
    public class NodeInfo
      {
        // global variables for NodeInfo
        String IP;
        String Name;
        int Port;
        
        // Initalization constructor for NodeInfo
        // allows for IP,name and port to be set to specified parameter
        public NodeInfo(String IP, String inName, int inPort)
          {
            this.IP = IP;
            Name = inName;
            Port = inPort;
          }
      }
  
    // Global variables for ChatNode
    static Receiver receiver = null;
    static Sender sender = null;
    
    public static NodeInfo myInfo;
    public static LinkedList<NodeInfo> participantList;

     // initalization constructor
     // allows for IP, Name and Port to be set
    public ChatNode(String inIp, String inName, int inPort)
      {
        myInfo = new NodeInfo(inIp, inName, inPort);

        participantList = new LinkedList<NodeInfo>();
      }

    // run reciever and sender threads
    public void run()
      {
        (receiver = new Receiver(myInfo)).start();
        (sender = new Sender()).start();
      }

     // main method creates new chat node 
     // by calling in the chatNode constructor
    public static void main(String[] args)
      {
        (new ChatNode(args[0], args[1], Integer.parseInt(args[2]))).run();
      }
  }

