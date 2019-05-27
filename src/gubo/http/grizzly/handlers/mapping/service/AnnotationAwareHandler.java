package gubo.http.grizzly.handlers.mapping.service;

import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.grizzly.handlers.InMemoryMultipartEntryHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *  
 *  包装{@link gubo.http.grizzly.handlergenerator.MappingToPath}标注的方法。
 *  
 *  
 **/
public class AnnotationAwareHandler extends NannyHttpHandler {
    private Object service;
    private Class<?> pclazz; // 接口方法的参数类型。
    private Method method;   // 接口方法。
    
    
    public AnnotationAwareHandler(Object service, Method method) {
        this.service = service;
        this.method = method;
        this.pclazz = ServiceUtil.getParameterClass(method);
    }
    
    
    public Object doXXX(Request request, Response response) throws Exception {
        Object p = pclazz.newInstance();
        this.bindParameter(request, p);
        
        Class<?>[] parameterTypes = this.method.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; ++i) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType == Request.class) {
                params[i] = request;
            } else if (parameterType == Response.class) {
                params[i] = response;
            } else if (parameterType == pclazz) {
                params[i] = p;
            } 
        }
        
        Object res = this.method.invoke(this.service, params);
        return res;
    }
    
    @Override
    public Object doGet(Request request, Response response) throws Exception {
        Object res = doXXX(request, response);
        return res;
    }
    
    @Override
    public Object doPost(Request request, Response response) throws Exception {
        Object res = doXXX(request, response);
        return res;
    }
    
    @Override
    public Object doPost(Request request, Response response, InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler) throws Exception {
        
        Object p = pclazz.newInstance();
        this.bindParameter(inMemoryMultipartEntryHandler.getMap(), p);
        
        Class<?>[] parameterTypes = this.method.getParameterTypes();
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; ++i) {
            Class<?> parameterType = parameterTypes[i];
            if (parameterType == Request.class) {
                params[i] = request;
            } else if (parameterType == Response.class) {
                params[i] = response;
            } else if (parameterType == InMemoryMultipartEntryHandler.class) {
                params[i] = inMemoryMultipartEntryHandler;
            } else if (parameterType == pclazz) {
                params[i] = p;
            } 
        }
        
        Object res = this.method.invoke(this.service, params);
        return res;
    }
    
    public static void f(String arg1, int ... numbers) {
        System.out.println(arg1);
        for (int i : numbers) {
            System.out.println(i);
        }
    }
    
    public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int[] numbers = new int[4];
        Object[] params = new Object[2];
        params[0] = "Sdf";
        params[1] = numbers;
        Method methods[] = AnnotationAwareHandler.class.getMethods();
        for(Method method : methods) {
            if (method.getName().equals("f")) {
                method.invoke(null, params); // <== This works as expected.
            }
        }
    }

}
