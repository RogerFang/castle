package com.whenling.castle.api;

import java.io.Serializable;

import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.whenling.castle.domain.Domain;

public interface DomainService<D extends Domain<I>, E extends Persistable<I>, I extends Serializable> {

	PagingAndSortingRepository<E, I> getRepository();

	void domainToEntity(D source, E target);

	void entityToDomain(E source, D target);

	@SuppressWarnings("unchecked")
	default Class<D> domainClass() {
		ResolvableType resolvableType = ResolvableType.forClass(getClass());
		return (Class<D>) resolvableType.as(DomainService.class).getGeneric(0).resolve();
	}

	@SuppressWarnings("unchecked")
	default Class<E> entityClass() {
		ResolvableType resolvableType = ResolvableType.forClass(getClass());
		return (Class<E>) resolvableType.as(DomainService.class).getGeneric(1).resolve();
	}

	default D newDomain() {
		try {
			return domainClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	default E newEntity() {
		try {
			return entityClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	default E toEntity(D domain) {
		if (domain == null) {
			return null;
		}

		E entity = domain.getId() == null ? newEntity() : getRepository().findOne(domain.getId());

		domainToEntity(domain, entity);

		return entity;
	}

	default D toDomain(E entity) {
		if (entity == null) {
			return null;
		}

		D domain = newDomain();
		if (!entity.isNew()) {
			domain.setId(entity.getId());
		}

		entityToDomain(entity, domain);

		return domain;
	}

}
