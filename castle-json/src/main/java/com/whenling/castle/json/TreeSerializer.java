package com.whenling.castle.json;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.base.Objects;
import com.whenling.castle.repo.domain.Node;
import com.whenling.castle.repo.domain.Tree;

public class TreeSerializer<T extends Tree<?>> extends JsonSerializer<T> {

	@Override
	public void serialize(T value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if (value.isCheckable()) {
			Set<?> checked = value.getChecked();
			visitNodes(value.getRoots(), node -> node.setChecked(isChecked(node, checked)));
		}
		if (value.isExpandAll()) {
			visitNodes(value.getRoots(), node -> node.setExpanded(true));
		}
		serializers.findValueSerializer(List.class, null).serialize(value.getRoots(), gen, serializers);
	}

	protected void visitNodes(List<? extends Node<?>> nodes, Consumer<Node<?>> consumer) {
		if (nodes != null) {
			nodes.forEach((node) -> {
				consumer.accept(node);
				visitNodes(node.getChildren(), consumer);
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

}
