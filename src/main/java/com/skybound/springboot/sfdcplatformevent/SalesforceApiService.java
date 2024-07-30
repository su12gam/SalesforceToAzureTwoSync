package com.skybound.springboot.sfdcplatformevent;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skybound.springboot.sfdcplatformevent.service.HttpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SalesforceApiService {
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	HttpService httpService;
	
	@Value("${salesforce.clientId}")
	private String salesforceClientId;
	
	@Value("${salesforce.clientSecret}")
	private String salesforceClientSecret;

	@Value("${salesforce.instance}")
	private String salesforceInstanceUrl;

	public String getOAuthAccessToken() throws Exception {

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		String token="";
		
		String requestBody = "grant_type=client_credentials" +
                "&client_id=" + salesforceClientId +
                "&client_secret=" + salesforceClientSecret;
		try {
			HttpResponse<String> response = httpService.call(com.skybound.springboot.sfdcplatformevent.service.HttpService.HttpMethod.POST, salesforceInstanceUrl+"/services/oauth2/token", headers,requestBody);
			
			if(200 == response.statusCode()) {
				token=this.extractAccessToken(response.body());
			}
			return token;
		} catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}
		return null;
	}

	private String extractAccessToken(String jsonResponse) throws IOException {
		JsonNode jsonNode = objectMapper.readTree(jsonResponse);
		return jsonNode.get("access_token").asText();
	}
}
