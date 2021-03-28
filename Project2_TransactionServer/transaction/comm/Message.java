package transaction.comm;

public class Message
{
    private String type;
    private Object[] content;

    public Message(String inType, Object[] inContent)
    {
        type = inType;
        content = inContent;
    }

    public String getType()
    {
        return type;
    }

    public Object[] getContent()
    {
        return content;
    }
}
