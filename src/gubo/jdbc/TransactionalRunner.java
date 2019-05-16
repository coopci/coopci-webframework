package gubo.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 我服了，让“工程师”把事务写全了确实是“太高”的要求，所以加上这个了。
 **/
public interface TransactionalRunner {

    public Connection getConnection() throws SQLException;
    default public void runInTransaction(TransactionalRunnable runnable) throws Exception {
        try(Connection dbconn = this.getConnection()) {
            dbconn.setAutoCommit(false);
            runnable.run(dbconn);
            dbconn.commit();
        }
    }
    
    default public <V> V callInTransaction(TransactionalCallable<V> callable) throws Exception {
        
        try(Connection dbconn = this.getConnection()) {
            dbconn.setAutoCommit(false);
            V ret = callable.call(dbconn);
            dbconn.commit();
            return ret;
        }
    }
}
