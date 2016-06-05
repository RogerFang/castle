package com.whenling.castle.support.repo.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseMongoRepository<T> extends MongoRepository<T, String>, QueryDslPredicateExecutor<T> {

}
