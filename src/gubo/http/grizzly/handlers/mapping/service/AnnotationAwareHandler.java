package gubo.http.grizzly.handlers.mapping.service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.grizzly.handlergenerator.LogParameters;
import gubo.http.grizzly.handlers.InMemoryMultipartEntryHandler;
import gubo.http.querystring.QueryStringBinder;
import gubo.http.service.ServiceUtil;

/**
 *  
 *  包装{@link gubo.http.grizzly.handlergenerator.MappingToPath}标注的方法。
 *  
 *  
 **/
public class AnnotationAwareHandler extends NannyHttpHandler {
    private static final Logger logger = LoggerFactory
            .getLogger(AnnotationAwareHandler.class);
    private Object service;
    private Class<?> pclazz; // 接口方法的参数类型。
    private Method method;   // 接口方法。
    
    boolean doLog = false;
    Logger loggerForMethod = LoggerFactory.getLogger(AnnotationAwareHandler.class);
	HashSet<String> hideFields = new HashSet<String>();
	boolean isNullOrVoid(Class<?> clazz) {
		if (clazz == null) {
			return true;
		}
		if (clazz == Void.class) {
			return true;
		}
		return false;
	}
    public int getDefaultSizeLimit() {
        return 1024*1024*10;
    }
    public AnnotationAwareHandler(Object service, Method method) {
        this.service = service;
        this.method = method;
        this.pclazz = ServiceUtil.getParameterClass(method);
        LogParameters[] annos = this.method.getAnnotationsByType(LogParameters.class);
        if (annos.length > 0) {
        	LogParameters anno = annos[0];
        	this.doLog = true;
        	if(!isNullOrVoid(anno.loggerClass())) {
        		loggerForMethod = LoggerFactory.getLogger(anno.loggerClass());
        	}
        	String[] hides = anno.hideFields().split(",");
        	for (String h : hides) {
        		this.hideFields.add(h);
        	}
        }
    }
    void logBindingErrorIfNeeded(InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler, Exception ex) throws UnsupportedEncodingException {
    	if (!this.doLog) {
    		return;
    	}
    	//把参数错误这件事 记日志
    	final QueryStringBinder binder = new QueryStringBinder();
    	Map<String, String> map = inMemoryMultipartEntryHandler.getMap();
    	
		for (String f : this.hideFields) {
			map.remove(f);
		}
		String paramsToLog = binder.toQueryString(map);
		String log = "Exception thrown when binding parameter for " + this.method.getName() + " " + paramsToLog;
		this.loggerForMethod.error(log, ex);
    	
    }
    void logBindingErrorIfNeeded(Request request, Exception ex) throws UnsupportedEncodingException {
    	if (!this.doLog) {
    		return;
    	}
    	//把参数错误这件事 记日志
    	final QueryStringBinder binder = new QueryStringBinder();
    	Map<String, String> map = QueryStringBinder.extractParameters(request);
		
		for (String f : this.hideFields) {
			map.remove(f);
		}
		String paramsToLog = binder.toQueryString(map);
		String log = "Exception thrown when binding parameter for " + this.method.getName() + " " + paramsToLog;
		this.loggerForMethod.error(log, ex);
    }
    
    void logParsedParameterIfNeeded(Request request, Object p) throws UnsupportedEncodingException {
    	if (!this.doLog) {
    		return;
    	}
    	try {
			QueryStringBinder binder = new QueryStringBinder();
//     			String paramsToLog = binder.toQueryString(p, null);
			HashMap<String, String> map = binder.toHashMap(p, null);
			for (String f : this.hideFields) {
				map.remove(f);
			}
			String paramsToLog = binder.toQueryString(map);
			String log = "Entering " + this.method.getName() + " " + paramsToLog;
			this.loggerForMethod.info(log);
		} catch (Exception e) {
			this.loggerForMethod.error("Logging failed for entrance of " + this.method.getName(), e);
		}
    }
    public Object doXXX(Request request, Response response) throws Exception {
    	Object p = null; 
        if (pclazz != null) {
        	p = pclazz.newInstance();
        	 try {
             	this.bindParameter(request, p);
             } catch (Exception ex) {
             	// 绑定参数出错，
             	logBindingErrorIfNeeded(request, ex);
             	
     			throw ex;
             }
        }
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
       
        logParsedParameterIfNeeded(request, p);
        try {
        	
            Object res = this.method.invoke(this.service, params);
            return res;
        } catch (InvocationTargetException ex) {
            logger.error("Exception thrown: ", ex);
            Throwable cause = ex.getCause();
            if (cause instanceof Exception) {
                throw (Exception)cause;
            } else {
                throw ex;
            }
            
        }
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
        
        Object p = null;
        if (pclazz != null) {
        	pclazz.newInstance();
	        
	//        this.bindParameter(inMemoryMultipartEntryHandler, p);
	        try {
	        	this.bindParameter(inMemoryMultipartEntryHandler, p);
	        } catch (Exception ex) {
	        	// 绑定参数出错，
	        	logBindingErrorIfNeeded(inMemoryMultipartEntryHandler, ex);
				throw ex;
	        }
    	}
        
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
        logParsedParameterIfNeeded(request, p);
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
