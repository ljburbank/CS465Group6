package transaction.server.lock;

import java.util.Hashtable;

import transaction.server.account.Account;
import transaction.server.lock.Lock.LockType;
import transaction.server.transaction.Transaction;

public class LockManager {
    private Hashtable<Account, Lock> locks;   //referencable table that maps accounts to locks
                               //note: accounts are the keys, locks are values.
    private Boolean useLocks;
    
    public LockManager(Boolean useLocks)
    {
        this.locks = new Hashtable<Account, Lock>();
        this.useLocks = useLocks;
    }
    
    //lock function obtains access to a given account
    public void lock(Account account, Transaction tran, String lockType )
    {
        if(! this.useLocks )
        {
            System.out.println("useLocks flag set to false; skipping lock action.");
            return;
        }
        
        //initialize variables
        Lock foundLock;
        
        
        
        //synchronized look up section to preserve lock integrity
        synchronized(this)
        {
            //if account has entry in table
            if(locks.containsKey(account))
            {
                //get the lock.
                foundLock = (Lock) locks.get(account);
            }
            //else lock does not yet exist
            else
            {
                //create lock
                foundLock = new Lock(account);
                        
                //add to hashtable
                locks.put(account, foundLock);
            }
        }
        
        //call acquire on the lock to set it
        foundLock.acquire(tran, lockType);
    }
    
    
    
    //unlock function releases all locks associated with a transaction
    public synchronized void unLock(Transaction tran)
    {
        if(! this.useLocks )
        {
            System.out.println("useLocks flag set to false; skipping lock action.");
            return;
        }
        
        //initialize variables
        int index;
        Lock workingLock;
        //get all of the locks held by the transaction
        Lock[] workingLocks = tran.locks;
        
        //for each lock held by the transaction
        for( index = 0; index < workingLocks.length; index ++)
        {
          //get lock at index
          workingLock = workingLocks[index];
        
        //skip empty indices in lock array
        if(workingLock == null )
          {
           continue;
          }

          //if lock held by transaction
          if(workingLock.holders.contains( tran.tranID ))
          {
              //release the lock  
              workingLock.release(tran.tranID);
          }
          
        }     
    }
}
