package chatapp;

class ChatNode
{
    static Receiver receiver = null;
    static Sender sender = null;

    public String ip, name;
    public int port;
    public ChatNode participantList[];

    public ChatNode(String inIp, String inName, int inPort)
    {
        ip = inIp;
        name = inName;
        port = inPort;
    }

    public void run()
    {
        (receiver = new Receiver(this)).start();
        (sender = new Sender()).start();
    }

    public static void main(String[] args)
    {
        (new ChatNode( args[0], args[1], Integer.parseInt(args[2]) )).run();
    }
}