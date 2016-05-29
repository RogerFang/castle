package com.whenling.castle.framework.repo.mongodb.entity;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;
import org.springframework.util.ClassUtils;

public class BaseMongoEntity<I extends Serializable> implements Persistable<I> {

	private static final long serialVersionUID = 7757703016339280358L;

	private I id;

	@Override
	public I getId() {
		return id;
	}

	protected void setId(final I id) {
		this.id = id;
	}

	@Override
	public boolean isNew() {
		return null == getId();
	}

	@Override
	public String toString() {
		return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
	}

	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += null == getId() ? 0 : getId().hashCode() * 31;
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {

		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		Class<?> thisClass = ClassUtils.getUserClass(getClass());
		Class<?> objClass = ClassUtils.getUserClass(obj.getClass());

		if (!(ClassUtils.isAssignable(thisClass, objClass) || ClassUtils.isAssignable(objClass, thisClass))) {
			return false;
		}

		BaseMongoEntity<?> that = (BaseMongoEntity<?>) obj;

		return null == this.getId() ? false : this.getId().equals(that.getId());

	}

}
