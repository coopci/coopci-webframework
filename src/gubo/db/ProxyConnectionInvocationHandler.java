package gubo.db;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyConnectionInvocationHandler implements InvocationHandler {

    Connection delegate;
    LeakTracker tracker;
    public ProxyConnectionInvocationHandler(Connection delegate, LeakTracker tracker) {
        this.delegate = delegate;
        this.tracker = tracker;
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
            this.tracker.remove(this.delegate);
        }
        return method.invoke(this.delegate, args);
    }
}