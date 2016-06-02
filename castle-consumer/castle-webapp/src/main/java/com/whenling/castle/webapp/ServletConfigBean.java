package com.whenling.castle.webapp;

import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.whenling.castle.support.config.ConfigurationPropertyResourceConfigurer;
import com.whenling.castle.support.config.StaticConfigSupplier;
import com.whenling.castle.support.core.ConsumerSupport;

@Configuration
@ComponentScan(basePackages = { "com.whenling" }, useDefaultFilters = false, includeFilters = { @Filter({ Controller.class }),
		@Filter({ ConsumerSupport.class }) }, nameGenerator = FullBeanNameGenerator.class)
@EnableWebMvc
public class ServletConfigBean {

	@Bean
	public static PlaceholderConfigurerSupport placeholderConfigurer() {
		return new ConfigurationPropertyResourceConfigurer(StaticConfigSupplier.getConfiguration());
	}
}
