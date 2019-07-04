package gubo.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

public class ProxyDataSourceInvocationHandler implements InvocationHandler {

    DataSource delegate;

    public ProxyDataSourceInvocationHandler(DataSource delegate) {
        this.delegate = delegate;
    }

    Method getConnection;
    {
        try {
            getConnection = DataSource.class.getMethod("getConnection");
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    ConcurrentHashMap<Connection, StackTraceElement[]> track = new ConcurrentHashMap<Connection, StackTraceElement[]>();
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (method.equals(getConnection)) {
            System.out.println("Intercepting getConnection");
            Connection conn = (Connection)method.invoke(this.delegate, args);
            
            ProxyConnectionInvocationHandler h = new ProxyConnectionInvocationHandler(conn, this.track);
            Connection ret = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                    new Class[] { Connection.class },
                    h);
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            track.put(conn, stack);
            return ret;
        }
        return method.invoke(this.delegate, args);
    }
}
