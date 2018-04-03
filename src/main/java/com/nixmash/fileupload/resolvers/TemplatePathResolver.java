package com.nixmash.fileupload.resolvers;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.mustachejava.MustacheResolver;
import com.github.mustachejava.resolver.ClasspathResolver;

import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

public class TemplatePathResolver extends ClasspathResolver implements MustacheResolver {

    public String populateTemplate(String template, Map<String, Object> model) {
        return getTemplate("templates/", template, model);
    }

    private String getTemplate(String resourceRoot, String template, Map<String, Object> model) {
        Reader reader = getReader(resourceRoot + template);
        MustacheFactory c = new DefaultMustacheFactory();
        Mustache m = c.compile(reader, template);
        StringWriter sw = new StringWriter();
        m.execute(sw, model);
        return sw.toString();
    }
}
