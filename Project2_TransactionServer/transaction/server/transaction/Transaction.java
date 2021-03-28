package transaction.server.transaction;

import transaction.server.lock.Lock;

public class Transaction
{
    public int tranID;
    public Lock[] locks;
    public String tranLog;

    public Transaction( int inID )
    {
        tranID = inID;
        locks = new Lock[2];

        tranLog = "Transaction " + Integer.toString(inID) + " Log\n";
        tranLog += "=================\n";
    }

    // Called by LockManager to give locks to a transaction
    // Assumes the transactions will all use two locks
    public void takeLock( Lock lock )
    {
        if( locks[0] == null )
        {
            locks[0] = lock;
        }
        else
        {
            locks[1] = lock;
        }
    }

    // Called by the TransactionManager to log what is happening in the transaction
    public void log( String strToLog )
    {
        tranLog += strToLog;
        tranLog += '\n';
    }

    // Called by the TransactionManager to display the transaction's log results
    public String getLog()
    {
        return tranLog;
    }

    // Called by the TransactionManager to log what transaction is affected
    public int getTransactionID()
    {
        return tranID;
    }
}
