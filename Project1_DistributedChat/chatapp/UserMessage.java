package chatapp;
import java.io.Serializable;
import java.util.LinkedList;

import chatapp.ChatNode.NodeInfo;

public class UserMessage implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public ChatNode.NodeInfo senderInfo;
    public String message;
    public LinkedList<NodeInfo> participantList;

    public UserMessage( ChatNode.NodeInfo sender, String messageToSend )
    {
        senderInfo = sender;
        message = messageToSend;
        participantList = null;
    }
}