package com.whenling.castle.integration.webapp;

import java.util.List;

import javax.xml.transform.Source;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.geo.format.DistanceFormatter;
import org.springframework.data.geo.format.PointFormatter;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.ProxyingHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whenling.castle.web.ServletSupport;

@ServletSupport
@Configuration
public class WebappConfigBean extends WebMvcConfigurerAdapter implements ApplicationContextAware {

	@Autowired
	@Qualifier("mvcConversionService")
	private ObjectFactory<ConversionService> conversionService;

	@Autowired
	private ObjectMapper objectMapper;

	private ApplicationContext applicationContext;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);

		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
		stringConverter.setWriteAcceptCharset(false);

		converters.add(new ByteArrayHttpMessageConverter());
		converters.add(stringConverter);
		converters.add(new ResourceHttpMessageConverter());
		converters.add(new SourceHttpMessageConverter<Source>());
		converters.add(new AllEncompassingFormHttpMessageConverter());

		MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(
				objectMapper);
		converters.add(jackson2HttpMessageConverter);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatter(DistanceFormatter.INSTANCE);
		registry.addFormatter(PointFormatter.INSTANCE);

		if (!(registry instanceof FormattingConversionService)) {
			return;
		}

		FormattingConversionService conversionService = (FormattingConversionService) registry;

		DomainClassConverter<FormattingConversionService> converter = new DomainClassConverter<FormattingConversionService>(
				conversionService);
		converter.setApplicationContext(applicationContext);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(sortResolver());
		argumentResolvers.add(pageableResolver());

		ProxyingHandlerMethodArgumentResolver resolver = new ProxyingHandlerMethodArgumentResolver(
				conversionService.getObject());
		resolver.setBeanFactory(applicationContext);
		resolver.setBeanClassLoader(applicationContext.getClassLoader());

		argumentResolvers.add(resolver);

		argumentResolvers.add(0, querydslPredicateArgumentResolver());
	}

	@Bean
	public PageableHandlerMethodArgumentResolver pageableResolver() {
		PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver(
				sortResolver());
		pageableHandlerMethodArgumentResolver.setPageParameterName("page");
		pageableHandlerMethodArgumentResolver.setOneIndexedParameters(true);
		pageableHandlerMethodArgumentResolver.setSizeParameterName("limit");
		return pageableHandlerMethodArgumentResolver;
	}

	@Bean
	public SortHandlerMethodArgumentResolver sortResolver() {
		return new SortHandlerMethodArgumentResolver();
	}

	@Bean
	public QuerydslPredicateArgumentResolver querydslPredicateArgumentResolver() {
		return new QuerydslPredicateArgumentResolver(querydslBindingsFactory(), conversionService.getObject());
	}

	@Lazy
	@Bean
	public QuerydslBindingsFactory querydslBindingsFactory() {
		QuerydslBindingsFactory querydslBindingsFactory = new QuerydslBindingsFactory(
				SimpleEntityPathResolver.INSTANCE);
		querydslBindingsFactory.setApplicationContext(applicationContext);
		return querydslBindingsFactory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
