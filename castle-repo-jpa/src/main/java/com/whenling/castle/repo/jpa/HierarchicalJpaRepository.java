package com.whenling.castle.repo.jpa;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import com.whenling.castle.repo.domain.Tree;

@NoRepositoryBean
public interface HierarchicalJpaRepository<T extends HierarchicalEntity<?, I, T>, I extends Serializable>
		extends BaseJpaRepository<T, I> {

	List<T> findRoots();

	List<T> findAllChildren(T current);

	Tree<T> findTree(T current);

	Tree<T> toTree(T current, List<T> nodes);
}
