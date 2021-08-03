package springless.http.grizzly;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

public class ModelAndTemplate {
	private JtwigModel model;
	public JtwigModel getModel() {
		return model;
	}
	public void setModel(JtwigModel model) {
		this.model = model;
	}
	public JtwigTemplate getTemplate() {
		return template;
	}
	public void setTemplate(JtwigTemplate template) {
		this.template = template;
	}
	private JtwigTemplate template;
	
	public ModelAndTemplate(JtwigModel m, JtwigTemplate t) {
		this.model = m;
		this.template = t;
	}
}
