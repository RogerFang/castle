package com.whenling.castle.integration.webapp.json;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import com.google.common.base.Objects;
import com.whenling.castle.json.FV;
import com.whenling.castle.repo.domain.Tree;

@ControllerAdvice
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class JsonResponseBodyAdvice extends AbstractMappingJacksonResponseBodyAdvice {

	@Override
	protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
			MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
		Object value = bodyContainer.getValue();
		if (value != null) {
			HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
			String jsonView = httpRequest.getParameter("json_view");

			Class<?> viewClass = FV.Simple.class;
			if (Objects.equal(jsonView, "detail")) {
				viewClass = FV.Detail.class;
			} else if (Objects.equal(jsonView, "simple")) {
				viewClass = FV.Simple.class;
			} else if (Objects.equal(jsonView, "full")) {
				viewClass = FV.Full.class;
			} else if (Objects.equal(jsonView, "audit")) {
				viewClass = FV.Audit.class;
			} else if (Objects.equal(jsonView, "none")) {
				throw new UnsupportedOperationException();
			}

			bodyContainer.setSerializationView(viewClass);

			if (value instanceof Tree) {
				((Tree<?>) value).setNodeView(viewClass);
			}
		}

	}

}
