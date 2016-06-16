package com.whenling.castle.json;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.whenling.castle.repo.domain.Node;
import com.whenling.castle.repo.domain.Tree;

public class TreeSerializer<T extends Tree<?>> extends JsonSerializer<T> {

	@Override
	public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if (value.isCheckable()) {
			visitCheck(value.getRoots(), value.getChecked());
		}
		if (!Strings.isNullOrEmpty(value.getIconProperty())) {
			visitIcon(value.getRoots(), value.getIconProperty());
		}
		if (value.isExpandAll()) {
			expandAll(value.getRoots());
		}
		serializers.findValueSerializer(List.class, null).serialize(value.getRoots(), gen, serializers);
	}

	private void visitCheck(List<? extends Node<?>> nodes, Set<?> checked) {
		if (nodes != null) {
			nodes.forEach((node) -> {
				node.setChecked(isChecked(node, checked));
				visitCheck(node.getChildren(), checked);
			});
		}
	}

	private boolean isChecked(Node<?> node, Set<?> checked) {
		if (checked != null) {
			for (Object c : checked) {
				if (Objects.equal(c, node.getData())) {
					return true;
				}
			}
		}

		return false;
	}

	private void expandAll(List<? extends Node<?>> nodes) {
		if (nodes != null) {
			nodes.forEach((node) -> {
				node.setExpanded(true);
				expandAll(node.getChildren());
			});
		}
	}

	private void visitIcon(List<? extends Node<?>> nodes, String iconProperty) {
		if (nodes != null) {
			nodes.forEach((node) -> {
				try {
					node.setIconCls(BeanUtils.getProperty(node.getData(), iconProperty));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				visitIcon(node.getChildren(), iconProperty);
			});
		}
	}
}
