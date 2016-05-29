package com.whenling.castle.provider.dubbo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.whenling.castle.framework.dubbo.DubboBeanPostProcessor;

@Configuration
@ComponentScan("com.whenling.castle.service")
public class DubboProviderConfigBean {

	@Bean
	public ApplicationConfig applicationConfig() {
		ApplicationConfig config = new ApplicationConfig("provider" + System.currentTimeMillis());
		return config;
	}

	@Bean
	public RegistryConfig registryConfig() {
		RegistryConfig config = new RegistryConfig();
		config.setAddress("multicast://224.5.6.7:1234");
		return config;
	}

	@Bean
	public ProtocolConfig protocolConfig() {
		ProtocolConfig config = new ProtocolConfig("dubbo", 20880);
		return config;
	}

//	@Bean
//	public DubboBeanScaner dubboBeanScaner() {
//		DubboBeanScaner scaner = new DubboBeanScaner();
//		scaner.setPackage("com.whenling.castle.service");
//		return scaner;
//	}

	@Bean
	public DubboBeanPostProcessor dubboBeanPostProcessor() {
		DubboBeanPostProcessor beanPostProcessor = new DubboBeanPostProcessor();
		beanPostProcessor.setPackage("com.whenling.castle.service");
		return beanPostProcessor;
	}
}
