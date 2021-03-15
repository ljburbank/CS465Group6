package chatapp;

public class NodeInfo
{
    private String ip, name;
    private int port;

    public NodeInfo( String inIp, String inName, int inPort )
    {
        ip = inIp;
        name = inName;
        port = inPort;
    }

    public String getIp()
    {
        return ip;
    }

    public String getName()
    {
        return name;
    }

    public int getPort()
    {
        return port;
    }
}
