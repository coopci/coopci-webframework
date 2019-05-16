package gubo.jdbc;

import java.sql.Connection;

public interface TrasactionalRunnable {
    public void run(Connection dbconn) throws Exception;
}
