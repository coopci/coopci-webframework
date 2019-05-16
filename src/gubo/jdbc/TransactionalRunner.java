package gubo.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface TransactionalRunner {

    public Connection getConnection() throws SQLException;
    default public void runInTransaction(TrasactionalRunnable runnable) throws Exception {
        try(Connection dbconn = this.getConnection()) {
            dbconn.setAutoCommit(false);
            runnable.run(dbconn);
            dbconn.commit();
        }
    }
    
    default public <V> V callInTransaction(TrasactionalCallable<V> callable) throws Exception {
        
        try(Connection dbconn = this.getConnection()) {
            dbconn.setAutoCommit(false);
            V ret = callable.call(dbconn);
            dbconn.commit();
            return ret;
        }
    }
}
