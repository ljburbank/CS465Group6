package transaction.server;

import transaction.server.lock.LockManager;
import transaction.server.account.AccountManager;
import transaction.server.transaction.TransactionManager;

public class TransactionServer
{
    public LockManager lockManager;
    public AccountManager accountManager;
    public TransactionManager transactionManager;

    public TransactionServer()
    {
        lockManager = new LockManager();
        accountManager = new AccountManager(10, 10);
        transactionManager = new TransactionManager();
    }

}
