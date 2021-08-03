package spingless.jdbc;

import java.sql.Connection;

public interface TransactionalRunnable {
    public void run(Connection dbconn) throws Exception;
}
