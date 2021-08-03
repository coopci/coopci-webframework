package springless.doc;

import java.util.List;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class JTwigRenderer {

	String render(List<ApiDocument> apiDocuments) throws Exception {
		JtwigTemplate jtwigTemplate = JtwigTemplate
				.classpathTemplate("gubo/doc/document.template.html");
		JtwigModel model = new JtwigModel();

		model.with("apiDocuments", apiDocuments);
		String ret = jtwigTemplate.render(model);

		return ret;
	}
}
