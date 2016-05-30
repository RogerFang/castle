package com.whenling.castle.framework.web.view;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer;
import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.whenling.castle.framework.web.view.groovy.CastleBaseTemplate;

/**
 * 视图配置
 * 
 * @author ken
 *
 */
@Configuration
public class ViewConfigBean {

	@Autowired
	private ServletContext servletContext;

	@Bean
	public GroovyMarkupViewResolver groovyMarkupViewResolver() {
		GroovyMarkupViewResolver resolver = new GroovyMarkupViewResolver();
		resolver.setContentType("text/html;charset=UTF-8");
		resolver.setSuffix(".tpl");
		resolver.setCache(false);
		return resolver;
	}

	@Bean
	public GroovyMarkupConfigurer groovyMarkupConfigurer() {
		GroovyMarkupConfigurer configurer = new GroovyMarkupConfigurer();
		configurer.setAutoIndent(true);
		configurer.setAutoNewLine(true);
		configurer.setBaseTemplateClass(CastleBaseTemplate.class);
		configurer.setCacheTemplates(false);
		configurer.setDeclarationEncoding("UTF-8");
		return configurer;
	}

	@Bean
	public ThymeleafViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine());
		resolver.setOrder(1);
		resolver.setViewNames(new String[] { "*.html", "*.xhtml" });
		return resolver;
	}

	//SpringResourceTemplateResolver:classpath
	@Bean
	public ServletContextTemplateResolver templateResolver() {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		// templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		return templateEngine;
	}

}
