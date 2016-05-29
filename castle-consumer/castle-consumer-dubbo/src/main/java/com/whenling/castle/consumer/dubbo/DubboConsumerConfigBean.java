package com.whenling.castle.consumer.dubbo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.whenling.castle.framework.core.ConsumerSupport;
import com.whenling.castle.framework.dubbo.DubboBeanPostProcessor;

@Configuration
@ConsumerSupport
public class DubboConsumerConfigBean {

	@Bean
	public ApplicationConfig applicationConfig() {
		ApplicationConfig config = new ApplicationConfig("consumer" + System.currentTimeMillis());
		return config;
	}

	@Bean
	public RegistryConfig registryConfig() {
		RegistryConfig config = new RegistryConfig();
		config.setAddress("multicast://224.5.6.7:1234");
		return config;
	}

	@Bean
	public DubboBeanPostProcessor dubboBeanPostProcessor() {
		DubboBeanPostProcessor beanPostProcessor = new DubboBeanPostProcessor();
		beanPostProcessor.setPackage("com.whenling.castle.webapp");
		return beanPostProcessor;
	}

}
