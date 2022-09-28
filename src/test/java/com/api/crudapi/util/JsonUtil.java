package com.api.crudapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonUtil {

	public static String asJsonString(final Object o) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonString = mapper.writeValueAsString(o);
			return jsonString;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
