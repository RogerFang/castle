package com.whenling.castle.json;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.base.Strings;
import com.whenling.castle.repo.domain.Hierarchical;
import com.whenling.castle.repo.domain.Node;

public class NodeSerializer<N extends Node<T>, T extends Hierarchical<T>> extends JsonSerializer<N> {

	@Override
	public void serialize(N value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if (value == null) {
			gen.writeNull();
			return;
		}

		gen.writeStartObject();

		gen.writeFieldName("children");
		serializers.findValueSerializer(List.class, null).serialize(value.getChildren(), gen, serializers);
		gen.writeBooleanField("leaf", value.getLeaf());

		if (value.getChecked() != null) {
			gen.writeBooleanField("checked", value.getChecked());
		}

		if (!Strings.isNullOrEmpty(value.getIconCls())) {
			gen.writeStringField("iconCls", value.getIconCls());
		}

		if (value.getExpanded() != null) {
			gen.writeBooleanField("expanded", value.getExpanded());
		}

		gen.writeFieldName("data");
		T data = value.getData();
		serializers.findValueSerializer(data.getClass()).serialize(data, gen, serializers);

		gen.writeEndObject();
	}

}
