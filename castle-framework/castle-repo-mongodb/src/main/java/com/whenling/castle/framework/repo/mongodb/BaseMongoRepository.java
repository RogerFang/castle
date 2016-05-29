package com.whenling.castle.framework.repo.mongodb;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseMongoRepository<T, I extends Serializable> extends MongoRepository<T, I>, QueryDslPredicateExecutor<T> {

}
