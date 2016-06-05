package com.whenling.castle.support.repo.mongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ClassUtils;

public class BaseMongoEntity implements Persistable<String> {

	private static final long serialVersionUID = 7757703016339280358L;

	@Id
	private String id;

	@Override
	public String getId() {
		return id;
	}

	protected void setId(final String id) {
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

		BaseMongoEntity that = (BaseMongoEntity) obj;

		return null == this.getId() ? false : this.getId().equals(that.getId());

	}

}
