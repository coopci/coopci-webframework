package gubo.jdbc;

import java.sql.Connection;

public interface TransactionalCallable<V> {
    public V call(Connection dbconn) throws Exception;
}
