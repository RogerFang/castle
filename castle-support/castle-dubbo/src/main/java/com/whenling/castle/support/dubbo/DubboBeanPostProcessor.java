package com.whenling.castle.support.dubbo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ModuleConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.alibaba.dubbo.config.spring.ServiceBean;
import com.whenling.castle.support.core.Consumer;
import com.whenling.castle.support.core.Provider;

public class DubboBeanPostProcessor implements DisposableBean, BeanPostProcessor, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(Logger.class);

	private ApplicationContext applicationContext;

	private String annotationPackage;
	private String[] annotationPackages;

	private final Set<ServiceConfig<?>> serviceConfigs = new ConcurrentHashSet<ServiceConfig<?>>();
	private final ConcurrentMap<String, ReferenceBean<?>> referenceConfigs = new ConcurrentHashMap<String, ReferenceBean<?>>();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (!isMatchPackage(bean)) {
			return bean;
		}
		Method[] methods = bean.getClass().getMethods();
		for (Method method : methods) {
			String name = method.getName();
			if (name.length() > 3 && name.startsWith("set") && method.getParameterTypes().length == 1 && Modifier.isPublic(method.getModifiers())
					&& !Modifier.isStatic(method.getModifiers())) {
				try {
					Consumer reference = method.getAnnotation(Consumer.class);
					if (reference != null) {
						Object value = refer(reference, method.getParameterTypes()[0]);
						if (value != null) {
							method.invoke(bean, new Object[] {});
						}
					}
				} catch (Throwable e) {
					logger.error("Failed to init remote service reference at method " + name + " in class " + bean.getClass().getName() + ", cause: "
							+ e.getMessage(), e);
				}
			}
		}
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}
				Consumer reference = field.getAnnotation(Consumer.class);
				if (reference != null) {
					Object value = refer(reference, field.getType());
					if (value != null) {
						field.set(bean, value);
					}
				}
			} catch (Throwable e) {
				logger.error("Failed to init remote service reference at filed " + field.getName() + " in class " + bean.getClass().getName()
						+ ", cause: " + e.getMessage(), e);
			}
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (!isMatchPackage(bean)) {
			return bean;
		}
		Provider provider = bean.getClass().getAnnotation(Provider.class);
		if (provider != null) {
			ServiceBean<Object> serviceConfig = new ServiceBean<Object>() {
				private static final long serialVersionUID = 3285792032951048299L;

				{
					appendAnnotation(Provider.class, provider);
				}
			};
			if (void.class.equals(provider.interfaceClass()) && "".equals(provider.interfaceName())) {
				if (bean.getClass().getInterfaces().length > 0) {
					serviceConfig.setInterface(bean.getClass().getInterfaces()[0]);
				} else {
					throw new IllegalStateException("Failed to export remote service class " + bean.getClass().getName()
							+ ", cause: The @Service undefined interfaceClass or interfaceName, and the service class unimplemented any interfaces.");
				}
			}
			if (applicationContext != null) {
				serviceConfig.setApplicationContext(applicationContext);
				if (provider.registry() != null && provider.registry().length > 0) {
					List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();
					for (String registryId : provider.registry()) {
						if (registryId != null && registryId.length() > 0) {
							registryConfigs.add((RegistryConfig) applicationContext.getBean(registryId, RegistryConfig.class));
						}
					}
					serviceConfig.setRegistries(registryConfigs);
				}
				if (provider.provider() != null && provider.provider().length() > 0) {
					serviceConfig.setProvider((ProviderConfig) applicationContext.getBean(provider.provider(), ProviderConfig.class));
				}
				if (provider.monitor() != null && provider.monitor().length() > 0) {
					serviceConfig.setMonitor((MonitorConfig) applicationContext.getBean(provider.monitor(), MonitorConfig.class));
				}
				if (provider.application() != null && provider.application().length() > 0) {
					serviceConfig.setApplication((ApplicationConfig) applicationContext.getBean(provider.application(), ApplicationConfig.class));
				}
				if (provider.module() != null && provider.module().length() > 0) {
					serviceConfig.setModule((ModuleConfig) applicationContext.getBean(provider.module(), ModuleConfig.class));
				}
				if (provider.provider() != null && provider.provider().length() > 0) {
					serviceConfig.setProvider((ProviderConfig) applicationContext.getBean(provider.provider(), ProviderConfig.class));
				} else {

				}
				if (provider.protocol() != null && provider.protocol().length > 0) {
					List<ProtocolConfig> protocolConfigs = new ArrayList<ProtocolConfig>();
					for (String protocolId : provider.registry()) {
						if (protocolId != null && protocolId.length() > 0) {
							protocolConfigs.add((ProtocolConfig) applicationContext.getBean(protocolId, ProtocolConfig.class));
						}
					}
					serviceConfig.setProtocols(protocolConfigs);
				}
				try {
					serviceConfig.afterPropertiesSet();
				} catch (RuntimeException e) {
					throw (RuntimeException) e;
				} catch (Exception e) {
					throw new IllegalStateException(e.getMessage(), e);
				}
			}
			serviceConfig.setRef(bean);
			serviceConfigs.add(serviceConfig);
			serviceConfig.export();
		}
		return bean;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void destroy() throws Exception {
		for (ServiceConfig<?> serviceConfig : serviceConfigs) {
			try {
				serviceConfig.unexport();
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		}
		for (ReferenceConfig<?> referenceConfig : referenceConfigs.values()) {
			try {
				referenceConfig.destroy();
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public String getPackage() {
		return annotationPackage;
	}

	public void setPackage(String annotationPackage) {
		this.annotationPackage = annotationPackage;
		this.annotationPackages = (annotationPackage == null || annotationPackage.length() == 0) ? null
				: Constants.COMMA_SPLIT_PATTERN.split(annotationPackage);
	}

	private Object refer(Consumer reference, Class<?> referenceClass) { // method.getParameterTypes()[0]
		String interfaceName;
		if (!"".equals(reference.interfaceName())) {
			interfaceName = reference.interfaceName();
		} else if (!void.class.equals(reference.interfaceClass())) {
			interfaceName = reference.interfaceClass().getName();
		} else if (referenceClass.isInterface()) {
			interfaceName = referenceClass.getName();
		} else {
			throw new IllegalStateException("The @Reference undefined interfaceClass or interfaceName, and the property type "
					+ referenceClass.getName() + " is not a interface.");
		}
		String key = reference.group() + "/" + interfaceName + ":" + reference.version();
		ReferenceBean<?> referenceConfig = referenceConfigs.get(key);
		if (referenceConfig == null) {
			referenceConfig = new ReferenceBean<Object>() {
				private static final long serialVersionUID = 515252412928482052L;
				{
					appendAnnotation(Consumer.class, reference);
				}
			};
			if (void.class.equals(reference.interfaceClass()) && "".equals(reference.interfaceName()) && referenceClass.isInterface()) {
				referenceConfig.setInterface(referenceClass);
			}
			if (applicationContext != null) {
				referenceConfig.setApplicationContext(applicationContext);
				if (reference.registry() != null && reference.registry().length > 0) {
					List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();
					for (String registryId : reference.registry()) {
						if (registryId != null && registryId.length() > 0) {
							registryConfigs.add((RegistryConfig) applicationContext.getBean(registryId, RegistryConfig.class));
						}
					}
					referenceConfig.setRegistries(registryConfigs);
				}
				if (reference.consumer() != null && reference.consumer().length() > 0) {
					referenceConfig.setConsumer((ConsumerConfig) applicationContext.getBean(reference.consumer(), ConsumerConfig.class));
				}
				if (reference.monitor() != null && reference.monitor().length() > 0) {
					referenceConfig.setMonitor((MonitorConfig) applicationContext.getBean(reference.monitor(), MonitorConfig.class));
				}
				if (reference.application() != null && reference.application().length() > 0) {
					referenceConfig.setApplication((ApplicationConfig) applicationContext.getBean(reference.application(), ApplicationConfig.class));
				}
				if (reference.module() != null && reference.module().length() > 0) {
					referenceConfig.setModule((ModuleConfig) applicationContext.getBean(reference.module(), ModuleConfig.class));
				}
				if (reference.consumer() != null && reference.consumer().length() > 0) {
					referenceConfig.setConsumer((ConsumerConfig) applicationContext.getBean(reference.consumer(), ConsumerConfig.class));
				}
				try {
					referenceConfig.afterPropertiesSet();
				} catch (RuntimeException e) {
					throw (RuntimeException) e;
				} catch (Exception e) {
					throw new IllegalStateException(e.getMessage(), e);
				}
			}
			referenceConfigs.putIfAbsent(key, referenceConfig);
			referenceConfig = referenceConfigs.get(key);
		}
		return referenceConfig.get();
	}

	private boolean isMatchPackage(Object bean) {
		if (annotationPackages == null || annotationPackages.length == 0) {
			return true;
		}
		String beanClassName = bean.getClass().getName();
		for (String pkg : annotationPackages) {
			if (beanClassName.startsWith(pkg)) {
				return true;
			}
		}
		return false;
	}

}