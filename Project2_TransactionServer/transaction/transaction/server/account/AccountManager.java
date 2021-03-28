package transaction.server.account;

import java.util.ArrayList;

import transaction.server.lock.*;
import transaction.server.transaction.Transaction;
import transaction.server.TransactionServer;

public class AccountManager
{
    private static ArrayList<Account> accounts;
    private static int numberAccounts;
    private static int initialBalance;

    public AccountManager(int inNumberAccounts, int inInitialBalance)
    {
        accounts = new ArrayList<>();
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

    // is this used anywhere??
    public ArrayList<Account> getAccounts()
    {
        return accounts;
    }
    
    public int read(int accountNumber, Transaction transaction)
    {
      // get the account
      Account account = getAccount(accountNumber);
      
      // set the read lock
      (TransactionServer.lockManager).lock(account, transaction, "READ_LOCK");
      
      // return the balance after reading the amount from account
      return (getAccount(accountNumber)).getBalance();
    }
    
    public int write(int accountNumber, int balance, Transaction transaction)
    {
      // get the account 
      Account account = getAccount(accountNumber);
      
      // set write lock
      (TransactionServer.lockManager).lock(account, transaction, "WRITE_LOCK");
      
      // sets balance after locking the account
      account.setBalance(balance);
      
      // return the updated balance
      return balance;
    }
}
