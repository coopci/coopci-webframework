package gubo.jdbc;

import java.sql.Connection;

public interface TrasactionalCallable<V> {
    public V call(Connection dbconn) throws Exception;
}
