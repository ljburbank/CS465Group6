package transaction.server.lock;

import java.util.ArrayList;
import java.util.Iterator;
import transaction.server.account.Account;
import transaction.;

public class Lock
  {
    //

    // acquire lock
    public synchronized void(
    Transaction transaction,
    int newLockType)throws TransactionAbortException
    {
      transaction.log(preFixLogString + " try " + getLockTypeString(newLockType) 
      + " on account #" +  account.getNumber());
      
      while(isConflict(transaction, newLockType))
      {
        ArrayList<Lock> locks = transaction.getLocks();
        Iterator<Lock> lockIterator = locks.iterator();
        Lock checkedLock;
        while(lockIterator.hasNext())
          {
            checkedLock = lockIterator.next();
            
            // loop to check each lock, abort if transaction if empty
            if(!checkLock.lockRequestors.isEmpty())
              {
                // display abort
                transaction.log(preFixLogToString +
                    " aborting when trying to set a " + getLockTypeString(newLockType) +
                    " on account #" + account.getNumber() +
                    " while holding a " + getLockTypeString(checkLock.currentLockType) + 
                    " on account #" + checkedLock.ccount.getID());
                
                throw new TransactionAbortException();
              }
          }
        
        transaction.log(preFixLogString +
            "---> wait to set " + getLockTypeString(newLockType) + 
            " on account #" + account.getNumber);
      }
    }
  }
