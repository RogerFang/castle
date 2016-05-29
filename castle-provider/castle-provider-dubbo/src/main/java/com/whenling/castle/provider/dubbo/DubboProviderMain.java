package com.whenling.castle.provider.dubbo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DubboProviderMain {

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DubboProviderConfigBean.class);
		context.start();

		System.in.read();
		context.close();
	}
}
