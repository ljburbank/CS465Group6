package transaction.server.account;

public class Account {

    private int id;
    private int balance;

    public Account( int inId, int inBalance )
    {
        this.id = inId;
        this.balance = inBalance;
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
