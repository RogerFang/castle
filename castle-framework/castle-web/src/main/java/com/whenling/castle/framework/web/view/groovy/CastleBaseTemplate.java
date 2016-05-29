package com.whenling.castle.framework.web.view.groovy;

import java.util.Map;

import com.whenling.castle.framework.web.WebSpringContext;

import groovy.text.markup.BaseTemplate;
import groovy.text.markup.MarkupTemplateEngine;
import groovy.text.markup.TemplateConfiguration;

public abstract class CastleBaseTemplate extends BaseTemplate {

	public CastleBaseTemplate(MarkupTemplateEngine templateEngine, Map<String, Object> model, Map<String, String> modelTypes,
			TemplateConfiguration configuration) {
		super(templateEngine, model, modelTypes, configuration);

		model.put("base", WebSpringContext.getContextPath());
	}

}
