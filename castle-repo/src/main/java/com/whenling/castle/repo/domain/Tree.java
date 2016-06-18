package com.whenling.castle.repo.domain;

import java.util.List;
import java.util.Set;

public interface Tree<T extends Hierarchical<T>> {

	List<Node<T>> getRoots();

	Set<T> getChecked();

	void setChecked(Set<T> checked);

	boolean isCheckable();

	boolean isExpandAll();

	void makeCheckable();

	void makeExpandAll();

	Class<?> getNodeView();

	void setNodeView(Class<?> viewClass);

}
