package com.whenling.castle.support.repo.mongodb;

import java.io.Serializable;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.QueryDslMongoRepository;
import org.springframework.data.querydsl.EntityPathResolver;

public class BaseMongoRepositoryImpl<T, I extends Serializable> extends QueryDslMongoRepository<T, I>implements BaseMongoRepository<T, I> {

	public BaseMongoRepositoryImpl(MongoEntityInformation<T, I> entityInformation, MongoOperations mongoOperations) {
		super(entityInformation, mongoOperations);
	}

	public BaseMongoRepositoryImpl(MongoEntityInformation<T, I> entityInformation, MongoOperations mongoOperations, EntityPathResolver resolver) {
		super(entityInformation, mongoOperations, resolver);
	}

}
