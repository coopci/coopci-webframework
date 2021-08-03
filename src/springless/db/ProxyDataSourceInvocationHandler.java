package springless.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import javax.sql.DataSource;

public class ProxyDataSourceInvocationHandler implements InvocationHandler {

    DataSource delegate;
    LeakTracker tracker;
    
    public ProxyDataSourceInvocationHandler(DataSource delegate, LeakTracker tracker) {
        this.delegate = delegate;
        this.tracker = tracker;
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
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (method.equals(getConnection)) {
            Connection conn = (Connection)method.invoke(this.delegate, args);
            
            ProxyConnectionInvocationHandler h = new ProxyConnectionInvocationHandler(conn, this.tracker);
            Connection ret = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                    new Class[] { Connection.class },
                    h);
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            tracker.add(conn, stack);
            return ret;
        }
        return method.invoke(this.delegate, args);
    }
}
