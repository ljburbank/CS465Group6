  
package transaction.server.lock;

import java.util.Vector;
import transaction.server.transaction.Transaction;

public class Lock
{
    //internal class keeps track of lock's type (read or write )
    public class LockType
        {
            private String lockType; //locktype int 0=read 1=write
            
            //constructor initializes locktype
            public LockType( String lockType )
            {
                this.lockType = lockType;
            }
    
            //promtes lock
            public void promote()
            {
                //if read lock
                if(lockType.equals("READ_LOCK") )
                {
                    lockType = "WRITE_LOCK";
                }
            }
            
            //getter method for lockType
            public String getType()
            {
                return lockType;
            }
    }
    
    
   //Fields 
   private Object account; //object to be protected by lock; accounts in this case
   public Vector holders; //tids of those currently holding this lock
   private Vector requestors;
   public LockType lockType; //either read or write
 
   //Lock constructor
   public Lock(Object account)
   {
       this.account = account;
   }
   
   //MANAGEMENT FUNCTIONS FOR holders FIELD
   public void addHolder( int tid )
   {
       holders.add(tid);
   }

   public void removeHolder(int tid)
   {
       holders.remove((Object) tid);
   }
   
   
   //LOCK ACCESS AND RELEASE FUNCTIONS
   
   //checks for conflict when a transaction requests a lock
   //returns false if no conflict
   //transaction refers to the transaction requesting the lock.
   private Boolean isConflicting(String requestedType, int transaction)
   {
       //check if holders list is empty
       if( this.holders.isEmpty() )
       {
           //if empty return true
           return false;
       }
       
       
       //else check if transaction only trans in holders
       else if (this.holders.contains((Object) transaction)
                && this.holders.size() == 1)
       {
           //return true
           return false;
       }
       
        //else check if requestedType is read and this.lockType is read
       else if (requestedType.equals("READ_LOCK")
                && this.lockType.getType().equals("READ_LOCK"))
       {
           //return true
           return false;
       }
       
       //else conflict exists, return false
       return true;
           
   }
   
   
   //called by lock manager to acquire a lock
   public synchronized void acquire(Transaction tran, String lockType )
   {
    //while there is a conflict
        while( isConflicting( lockType, tran.tranID ) )
        {
            //add to requesters
            requestors.add((Object) tran.tranID );
            
            //wait until no conflict
             try{
                 wait();
             }
             catch( InterruptedException e ){}
             
            //remove from requesters
            requestors.remove((Object) tran.tranID);
        }
       
    //process lock request
    //if no holders
    if(holders.isEmpty())
    {
       //add holder
       holders.add((Object) tran.tranID );
        
       //set lock type to requested lock type 
       this.lockType.lockType = lockType;
    }
       
    //else if there are other holders (must be read due to conflict checking)
    else if(this.lockType.equals("READ_LOCK"))
    {
        //if this transaction is not a holder
        if(!holders.contains((Object) tran.tranID))
        {
            //add this transaction as a holder
            //NOTE: locktype already set in this case
            holders.add((Object) tran.tranID );
        }
    }
    
    //else if this transaction is a holder, needs a more exclusive lock
    else if(holders.contains((Object) tran.tranID)
            && lockType.equals("WRITE_LOCK"))
    {
        //promote locktype
        this.lockType.promote();
    }
}
   
   
   public synchronized void release( int transactionId)
   {
       //remove specified element as a holder
       holders.removeElement((Object) transactionId);
       
       //set locktype to none
       this.lockType.lockType = "";
       
       //notify requestors
       notifyAll();
       
       
   }
}