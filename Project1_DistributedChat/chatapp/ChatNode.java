package chatapp;

import java.util.LinkedList;

public class ChatNode
  {

    public class NodeInfo
      {
        String IP;
        String Name;
        int Port;

        public NodeInfo(String IP, String inName, int inPort)
          {
            this.IP = IP;
            Name = inName;
            Port = inPort;
          }
      }

    static Receiver receiver = null;
    static Sender sender = null;

    public static NodeInfo myInfo;
    public static LinkedList<NodeInfo> participantList;

    public ChatNode(String inIp, String inName, int inPort)
      {
        myInfo = new NodeInfo(inIp, inName, inPort);

        participantList = new LinkedList<NodeInfo>();
      }

    public void run()
      {
        (receiver = new Receiver(myInfo)).start();
        (sender = new Sender()).start();
      }

    public static void main(String[] args)
      {
        (new ChatNode(args[0], args[1], Integer.parseInt(args[2]))).run();
      }
  }

