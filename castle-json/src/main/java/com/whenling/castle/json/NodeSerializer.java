package com.whenling.castle.json;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.base.MoreObjects;
import com.whenling.castle.core.SpringContext;
import com.whenling.castle.json.FV.Simple;
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

		if (value.getExpanded() != null) {
			gen.writeBooleanField("expanded", value.getExpanded());
		}

		T data = value.getData();
		if (data != null) {
			Class<?> nodeView = MoreObjects.firstNonNull(value.getTree().getNodeView(), Simple.class);
			String dataString = SpringContext.getBean(ObjectMapper.class).writerWithView(nodeView)
					.writeValueAsString(data);
			dataString = StringUtils.removeStart(dataString, "{");
			dataString = StringUtils.removeEnd(dataString, "}");
			gen.writeRaw("," + dataString);
		}

		gen.writeEndObject();
	}

}
