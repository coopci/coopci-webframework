package springless.http.grizzly.demo;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import springless.http.grizzly.ModelAndTemplate;
import springless.http.grizzly.NannyHttpHandler;

import java.io.File;

public class HtmlHandler extends NannyHttpHandler {

    @Override
    public Object doGet(Request request, Response response) throws Exception {
        JtwigTemplate t = JtwigTemplate.fileTemplate(new File("templates/simple.twig"));
        JtwigModel m = JtwigModel.newModel();
        m.with("var", "World");
        ModelAndTemplate ret = new ModelAndTemplate(m, t);


        return ret;
    }
}
