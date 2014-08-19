package com.rokagram.peek.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ServletUtils {
	private static ObjectMapper mapper = new ObjectMapper();

	public static ObjectMapper getMapper() {
		return mapper;
	}

	/**
	 * Attempt to serialize given object to JSON and write to output stream
	 * 
	 * @param resp
	 * @param object
	 * 
	 * @throws IOException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 */
	public static void writeResponseJson(HttpServletResponse resp, Object object) throws IOException,
			JsonGenerationException, JsonMappingException {

		resp.setContentType("application/json");
		addAccessControlAllowEverything(resp);
		mapper.writeValue(resp.getOutputStream(), object);
	}

	public static void addAccessControlAllowEverything(HttpServletResponse resp) {
		resp.addHeader("Access-Control-Allow-Origin", "*");
	}

	/**
	 * Useful for debug/admin interface
	 * 
	 * @param object
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String getIndentedJSON(Object object) throws JsonProcessingException {
		String ret = "";
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		ret = mapper.writeValueAsString(object);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
		return ret;

	}

}
