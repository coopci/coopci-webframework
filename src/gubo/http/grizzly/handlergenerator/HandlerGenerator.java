package gubo.http.grizzly.handlergenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import gubo.http.grizzly.NannyHttpHandler;

/**
 * 给定一个java接口，为每个方法生成一个对应的 NannyHttpHandler的子类。
 * 
 **/
public class HandlerGenerator {
	public static class SourceFile {
		String filename;
		String source;
		String addHandlerCode;
		
		// install handler 时用的 interface 的对象的名字。
		String itfObjName;
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();

			sb.append("============ ");
			sb.append(filename);
			sb.append(": \n");
			sb.append(this.source);
			sb.append("\n\n");
			return sb.toString();
		}

	}

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

	public String generateSource(String srcRoot, Class<?> interfc,
			String packageName, boolean doWriteFile, String installerClassName) throws SQLException,
			IOException {

		Method[] methods = interfc.getMethods();

		List<SourceFile> sourceFiles = new LinkedList<SourceFile>();
		for (Method m : methods) {
			MappingToPath[] mappings = m
					.getAnnotationsByType(MappingToPath.class);
			if (mappings.length == 0) {
				continue;
			}

			SourceFile sf = generateSource(interfc, m, packageName);
			if (sf == null) {
				continue;
			}
			sourceFiles.add(sf);
			System.out.println(sf);

			if (doWriteFile) {

				Path packagePath = Paths.get(srcRoot,
						packageName.replace(".", "/"));
				packagePath.toFile().mkdirs();
				Path filePath = Paths.get(packagePath.toString(), sf.filename);
				File file = filePath.toFile();
				if (!file.exists()) {
					file.createNewFile();
				}

				FileInputStream ins = new FileInputStream(file);
				OutputStream outs = new FileOutputStream(file);
				outs.write(sf.source.getBytes());
				outs.close();
				ins.close();
			}
		}

		for (SourceFile sf : sourceFiles) {
			System.out.println(sf.addHandlerCode);
		}
		
		
		if (!Strings.isNullOrEmpty(installerClassName)) {

			OutputStream outs = openInstallerClassForWrite(srcRoot, packageName, installerClassName);
			
			
			JtwigTemplate jtwigTemplate = JtwigTemplate
					.classpathTemplate("gubo/http/grizzly/handlergenerator/handler-installer.template");
			JtwigModel model = new JtwigModel();

			model.with("packageName", packageName);
			model.with("installerClassname", installerClassName);
			model.with("interfaceFullname", interfc.getName());
			
			if (sourceFiles.size() > 0) {
				model.with("interfaceVarname", sourceFiles.get(0).itfObjName);	
			} else {
				model.with("interfaceVarname", "itfObj");
			}
			
			
			LinkedList<String> addHandlers = new LinkedList<String>();
			for (SourceFile sf : sourceFiles) {
				addHandlers.add(sf.addHandlerCode);
			}
				
			model.with("addHandlers", addHandlers);
			String ret = jtwigTemplate.render(model);
			
			outs.write(ret.getBytes());
			outs.close();
		}
		
		return "";
	}
	
	OutputStream openInstallerClassForWrite(String srcRoot, String packageName, String installerClassName) throws IOException {
		Path packagePath = Paths.get(srcRoot,
				packageName.replace(".", "/"));
		Path filePath = Paths.get(packagePath.toString(), installerClassName+".java");
		File file = filePath.toFile();
		if (!file.exists()) {
			file.createNewFile();
		}
		
		OutputStream outs = new FileOutputStream(file);
		return outs;
	}

	public SourceFile generateSource(Class<?> interfc, Method method,
			String packageName) throws SQLException {

		Preconditions.checkArgument(this.getSuperClass() != null,
				"Super class must not be null.");

		StringBuilder sb = new StringBuilder();

		Method interfaceSetter = null;
		Method interfaceGetter = null;
		for (Method m : this.getSuperClass().getMethods()) {
			if (m.isAnnotationPresent(InterfaceSetter.class)) {
				interfaceSetter = m;
			} else if (m.isAnnotationPresent(InterfaceGetter.class)) {
				interfaceGetter = m;
			}
		}

		Preconditions.checkArgument(interfaceSetter != null,
				"Super class must have a InterfaceSetter.");

		Preconditions.checkArgument(interfaceGetter != null,
				"Super class must have a InterfaceGetter.");

		String pkg = "package " + packageName + ";\n";
		sb.append(pkg);

		sb.append("// This file is generated with "
				+ this.getClass().getCanonicalName());

		String parameterClass = method.getParameterTypes()[0]
				.getCanonicalName();

		String handlerName = method.getName() + "Handler";
		handlerName = handlerName.substring(0, 1).toUpperCase()
				+ handlerName.substring(1);

		// String imports = "import " + interfc.getCanonicalName()
		// + "; // interface type\n" + "import " + parameterClass
		// + "; // Parameter type\n"
		// + "import org.glassfish.grizzly.http.server.Request;\n"
		// + "import org.glassfish.grizzly.http.server.Response;\n";

		String imports = "import org.glassfish.grizzly.http.server.Request;\n"
				+ "import org.glassfish.grizzly.http.server.Response;\n"
				+ "import org.slf4j.Logger;\n"  
				+ "import org.slf4j.LoggerFactory;\n";

		sb.append("\n");
		sb.append(imports);

		String classDef = "public class " + handlerName + " extends "
				+ this.getSuperClass().getCanonicalName() + " {\n";
		sb.append(classDef);

		String ctor = String.format("    public %s(%s itf) {\n"
				+ "        this.%s(itf);\n" + "    }\n", handlerName,
				interfc.getName(), interfaceSetter.getName());

		sb.append("    Logger logger = LoggerFactory.getLogger(getClass());\r\n");
		sb.append(ctor);

		String httpmethod = "post";
		MappingToPath[] mappings = method
				.getAnnotationsByType(MappingToPath.class);
		httpmethod = mappings[0].method().toLowerCase();

		httpmethod = httpmethod.toUpperCase().substring(0, 1)
				+ httpmethod.toLowerCase().substring(1);

		String doXXXTemplate = "    @Override\n" + "    public Object do"
				+ httpmethod
				+ "(Request request, Response response) throws Exception {\n"
				+ "        logger.info(\"request received, uri: {}, request data: {}\", request.getRequestURI(),\r\n" + 
				"				request.getInputBuffer().getBuffer().toStringContent());\n"
				+ "        %s p = new %s();\n"
				+ "        this.bindParameter(request, p);\n"
				+ "        Object res = this.%s().%s(p);\n"
				+ "        return res;\n" + "    }\n";

		String doXXX = String.format(doXXXTemplate, parameterClass,
				parameterClass, interfaceGetter.getName(), method.getName());

		sb.append(doXXX);

		sb.append("}\n");
		sb.append("\n");

		SourceFile sf = new SourceFile();
		sf.filename = handlerName + ".java";
		sf.source = sb.toString();

		String itfObjName = interfaceSetter.getName();
		if (itfObjName.startsWith("set")) {
			itfObjName = itfObjName.substring(3);
		}
		itfObjName = itfObjName.substring(0, 1).toLowerCase()
				+ itfObjName.substring(1);

		String path = method.getName();
		if (mappings.length > 0) {
			path = mappings[0].value();
		}

		String addHandlerCode = String.format(
				"server.getServerConfiguration()\r\n"
						+ "        .addHttpHandler(new %s(%s), \"%s\");",
				handlerName, itfObjName, path);
		
		sf.addHandlerCode = addHandlerCode;
		sf.itfObjName = itfObjName;
		return sf;
	}
}
