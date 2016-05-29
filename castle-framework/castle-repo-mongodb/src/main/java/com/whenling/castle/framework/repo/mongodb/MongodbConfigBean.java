package com.whenling.castle.framework.repo.mongodb;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.google.common.collect.Lists;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@EnableMongoRepositories(basePackages = "com.whenling", repositoryImplementationPostfix = "Impl", repositoryFactoryBeanClass = CustomMongoRepositoryFactoryBean.class)
@EnableMongoAuditing
public class MongodbConfigBean {

	@Bean
	public MongoTemplate mongoTemplate() {
		MongoTemplate mongoTemplate = new MongoTemplate(mongo(), "admin");
		return mongoTemplate;
	}

	@Bean
	public Mongo mongo() {
		try {
			MongoClient mongoClient = new MongoClient(new ServerAddress("112.74.132.227", 27017),
					Lists.newArrayList(MongoCredential.createCredential("kongxx", "admin", "qwe123".toCharArray())));
			return mongoClient;
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
}
