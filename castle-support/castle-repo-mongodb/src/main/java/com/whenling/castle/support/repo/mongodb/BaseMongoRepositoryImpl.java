package com.whenling.castle.support.repo.mongodb;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.QueryDslMongoRepository;
import org.springframework.data.querydsl.EntityPathResolver;

public class BaseMongoRepositoryImpl<T> extends QueryDslMongoRepository<T, String>implements BaseMongoRepository<T> {

	public BaseMongoRepositoryImpl(MongoEntityInformation<T, String> entityInformation,
			MongoOperations mongoOperations) {
		super(entityInformation, mongoOperations);
	}

	public BaseMongoRepositoryImpl(MongoEntityInformation<T, String> entityInformation, MongoOperations mongoOperations,
			EntityPathResolver resolver) {
		super(entityInformation, mongoOperations, resolver);
	}

}
