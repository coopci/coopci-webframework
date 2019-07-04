package gubo.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyConnectionInvocationHandler implements InvocationHandler {

    Connection delegate;
    ConcurrentHashMap<Connection, StackTraceElement[]> track;
    public ProxyConnectionInvocationHandler(Connection delegate, ConcurrentHashMap<Connection, StackTraceElement[]> track) {
        this.delegate = delegate;
        this.track = track;
    }

    Method close;
    {
        try {
            close = Connection.class.getMethod("close");
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
        if (method.equals(close)) {
            System.out.println("Intercepting close");
            this.track.remove(this.delegate);
        }
        return method.invoke(this.delegate, args);
    }
}