package com.rokagram.peek.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServletUtils {
	private static ObjectMapper mapper = new ObjectMapper();

	public static ObjectMapper getMapper() {
		return mapper;
	}

	public static void writeResponseJson(HttpServletRequest req, HttpServletResponse resp, Object object)
			throws IOException, JsonGenerationException, JsonMappingException {

		resp.setContentType("application/json");
		mapper.writeValue(resp.getOutputStream(), object);
	}

}
