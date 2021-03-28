package transaction.server.account;

public class Account {

    private static int id;
    private int balance;

    public Account( int inId, int inBalance )
    {
        id = inId;
        balance = inBalance;
    }

    public int getBalance()
    {
        return balance;
    }

    public void setBalance(int inBalance)
    {
        balance = inBalance;
    }

    public int getId()
    {
        return id;
    }
}
