package chatapp;

public class UserMessage
{
    public NodeInfo senderInfo;
    public String message;

    public UserMessage( NodeInfo sender, String messageToSend )
    {
        senderInfo = sender;
        message = messageToSend;
    }
}