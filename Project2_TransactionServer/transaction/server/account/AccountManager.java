package transaction.server.account;

import java.util.ArrayList;

public class AccountManager
{
    private static ArrayList<Account> accounts;
    private static int numberAccounts;
    private static int initialBalance;

    public AccountManager(int inNumberAccounts, int inInitialBalance)
    {
        accounts = new ArrayList<Account>();
        numberAccounts = inNumberAccounts;
        initialBalance = inInitialBalance;

        for (int i = 0; i < numberAccounts; i++)
        {
            accounts.add(i, new Account(i, initialBalance));
        }
    }

    public Account getAccount(int accountNumber)
    {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAccounts()
    {
        return accounts;
    }
}
