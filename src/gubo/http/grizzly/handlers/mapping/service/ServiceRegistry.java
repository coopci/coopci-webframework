package gubo.http.grizzly.handlers.mapping.service;

import gubo.http.grizzly.handlergenerator.MappingToPath;
import javassist.Modifier;
import org.glassfish.grizzly.http.server.HttpServer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 把service 的各个接口方法（被  {@link gubo.http.grizzly.handlergenerator.MappingToPath}标注的方法 ）注册到httpserver上。 
 * 
 **/
public class ServiceRegistry {
    
    public static Method[] getAllMethodsInHierarchy(Class<?> objectClass) {
        Set<Method> allMethods = new HashSet<Method>();
        Method[] declaredMethods = objectClass.getDeclaredMethods();
        Method[] methods = objectClass.getMethods();
        if (objectClass.getSuperclass() != null) {
            Class<?> superClass = objectClass.getSuperclass();
            Method[] superClassMethods = getAllMethodsInHierarchy(superClass);
            allMethods.addAll(Arrays.asList(superClassMethods));
        }
        allMethods.addAll(Arrays.asList(declaredMethods));
        allMethods.addAll(Arrays.asList(methods));
        return allMethods.toArray(new Method[allMethods.size()]);
    }
    
    static public void register(Object service, HttpServer server, String prefix) {
        register(service, server, prefix, AnnotationAwareHandler.class);
    }
    
    static public void register(Object service, HttpServer server, String prefix, Class<? extends AnnotationAwareHandler> handlerClass) {
        
        for(Method method: getAllMethodsInHierarchy(service.getClass())) {
            int mod = method.getModifiers();
            if(Modifier.isStatic(mod)) {
                continue;
            }
            MappingToPath mp = method.getAnnotation(MappingToPath.class);
            if (mp == null) {
                continue;
            }
            AnnotationAwareHandler handler = new AnnotationAwareHandler(service, method);
            
            String path = mp.value();
            String pathWithoutTrailingSlash = prefix + "/" + path;
            if (pathWithoutTrailingSlash.endsWith("/")) {
                pathWithoutTrailingSlash = pathWithoutTrailingSlash.substring(0, pathWithoutTrailingSlash.length()-1);
            }
            String pathWithTrailingSlash = pathWithoutTrailingSlash + "/";
            server.getServerConfiguration().addHttpHandler(
                    handler, pathWithTrailingSlash, pathWithoutTrailingSlash);
        }
    }
}
