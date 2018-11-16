package gubo.http.grizzly.handlergenerator;

import gubo.http.grizzly.NannyHttpHandler;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 给定一个java接口，为每个方法生成一个对应的 NannyHttpHandler的子类。
 * 
 **/
public class HandlerGenerator {

    private Class<? extends NannyHttpHandler> superClass;
    private String interfacePropertyName;
    
    public String getInterfacePropertyName() {
        return interfacePropertyName;
    }

    public void setInterfacePropertyName(String interfacePropertyName) {
        this.interfacePropertyName = interfacePropertyName;
    }

    public Class<? extends NannyHttpHandler> getSuperClass() {
        return superClass;
    }

    public void setSuperClass(Class<? extends NannyHttpHandler> superClass) {
        this.superClass = superClass;
    }

    public String generateSource(Class<?> interfc, String packageName)
            throws SQLException {


        Method[] methods = interfc.getMethods();

        for (Method m : methods) {

            generateSource(interfc, m, packageName);

        }

        return "";
    }

    public String generateSource(Class<?> interfc, Method method, String packageName)
            throws SQLException {
        StringBuilder sb = new StringBuilder();
        
        Method interfaceSetter = null;
        Method interfaceGetter = null;
        for (Method m : this.getSuperClass().getMethods()) {
            if(m.isAnnotationPresent(InterfaceSetter.class)) {
                interfaceSetter = m;
            } else if(m.isAnnotationPresent(InterfaceGetter.class)) {
                interfaceGetter = m;
            }
        }
        
        
        String pkg = "package " + packageName + ";\n";
        sb.append(pkg);
        
        sb.append("// This file is generated with " + this.getClass().getCanonicalName());

        String parameterClass = method.getParameterTypes()[0].getCanonicalName();

        String handlerName = method.getName() + "Handler";
        handlerName = handlerName.substring(0, 1).toUpperCase() + handlerName.substring(1);

        String imports = "import " + interfc.getCanonicalName() + "; // interface type\n" +
                "import " + parameterClass + "; // Parameter type\n" +
                "import org.glassfish.grizzly.http.server.Request;\n" +
                "import org.glassfish.grizzly.http.server.Response;\n";

        sb.append("\n");
        sb.append(imports);
        
        
        String classDef = "public class " + handlerName + " extends " + this.getSuperClass().getCanonicalName() + " {\n";
        sb.append(classDef);
        
        String ctor = String.format("    public %s(%s itf) {\n" +  
                "        this.%s(itf);\n" + 
                "    }\n", 
                handlerName, 
                interfc.getName(), 
                interfaceSetter.getName()); 
        
        sb.append(ctor);
        
        
        
        String doXXXTemplate = "    @Override\n" + 
                "    public Object doPost(Request request, Response response) throws Exception {\n" + 
                "        %s p = new %s();\n" + 
                "        this.bindParameter(request, p);\n" + 
                "        Object res = this.%s().%s(p);\n" + 
                "        return res;\n" + 
                "    }\n";
        
        String doXXX = String.format(doXXXTemplate, method.getParameterTypes()[0].getName(), 
                method.getParameterTypes()[0].getName(),
                interfaceGetter.getName(),
                method.getName());
        
        sb.append(doXXX);
        
        
        sb.append("}\n");
        sb.append("\n");
        System.out.println(sb.toString());


        return "";
    }

}
