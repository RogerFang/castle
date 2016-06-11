package com.whenling.castle.repo.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import com.google.common.base.Objects;
import com.whenling.castle.repo.domain.Node;
import com.whenling.castle.repo.domain.SortNoComparator;
import com.whenling.castle.repo.domain.Tree;
import com.whenling.castle.repo.domain.TreeImpl;

public class HierarchicalJpaRepositoryImpl<T extends HierarchicalEntity<?, I, T>, I extends Serializable>
		extends BaseJpaRepositoryImpl<T, I>implements HierarchicalJpaRepository<T, I> {

	public HierarchicalJpaRepositoryImpl(JpaEntityInformation<T, I> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
	}

	@Override
	public List<T> findRoots() {
		return getQuery(new ParentSpecification<>(null, "parent"), (Sort) null).getResultList();
	}

	@Override
	public List<T> findAllChildren(T current) {
		return getQuery(new AllChildrenSpecification<>(current, "treePath"), (Sort) null).getResultList();
	}

	@Override
	public Tree<T> findTree(T current) {
		List<T> allChildren = current == null ? findAll() : findAllChildren(current);
		return toTree(current, allChildren);
	}

	@Override
	public Tree<T> toTree(T current, List<T> nodes) {
		Collections.sort(nodes, SortNoComparator.COMPARATOR);
		List<Node<T>> directSubordinates = findDirectSubordinates(current, nodes);
		if (current != null) {
			Node<T> root = toNode(current, directSubordinates);
			directSubordinates = new ArrayList<>();
			directSubordinates.add(root);
		}
		return new TreeImpl<>(directSubordinates);
	}

	protected List<Node<T>> findDirectSubordinates(T root, List<T> allChildren) {
		List<Node<T>> nodes = new ArrayList<>();
		for (T entity : allChildren) {
			if (Objects.equal(entity.getParent(), root)) {
				nodes.add(toNode(entity, findDirectSubordinates(entity, allChildren)));
			}
		}
		return nodes;
	}

	protected Node<T> toNode(T entity, List<Node<T>> children) {
		Node<T> node = new Node<>();
		node.setData(entity);
		node.setChildren(children);
		return node;
	}
}
