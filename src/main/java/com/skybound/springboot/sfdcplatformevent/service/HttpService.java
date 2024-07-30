package com.skybound.springboot.sfdcplatformevent.service;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HttpService {

	@Autowired
	HttpClient httpClient;

	@Autowired
	ObjectMapper objectMapper;

	public HttpResponse<String> call(HttpMethod requestType, String url) throws Exception {
		return call(requestType, url, null, null);
	}

	public HttpResponse<String> call(HttpMethod requestType, String url, Map<String, String> headersMap)
			throws Exception {
		return call(requestType, url, headersMap, null);
	}

	public HttpResponse<String> call(HttpMethod requestType, String url, Map<String, String> headersMap, Object data)
			throws Exception {

		String[] headers = headersMap.entrySet().stream().flatMap(entry -> Stream.of(entry.getKey(), entry.getValue()))
				.toArray(String[]::new);

		HttpRequest request = null;
		HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url)).headers(headers);

		if (HttpMethod.GET.equals(requestType)) {
			request = requestBuilder.GET().build();
		} else if (HttpMethod.POST.equals(requestType)) {
			request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(data.toString())).build();
		} else if (HttpMethod.PUT.equals(requestType)) {
			request = requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(data.toString())).build();
		} else if (HttpMethod.DELETE.equals(requestType)) {
			request = requestBuilder.DELETE().build();
		} else if (HttpMethod.PATCH.equals(requestType)) {
			request = requestBuilder.method("PATCH", HttpRequest.BodyPublishers.ofString(data.toString())).build();
		} else {
			throw new Exception("Unknown HTTP Request type.");
		}
		return httpClient.send(request, BodyHandlers.ofString());

	}

	public enum HttpMethod {
		GET, POST, PUT, DELETE, PATCH
	}

}
