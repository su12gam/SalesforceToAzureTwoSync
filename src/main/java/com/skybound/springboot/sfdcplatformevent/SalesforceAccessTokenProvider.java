package com.skybound.springboot.sfdcplatformevent;

import com.google.gson.Gson;
import com.salesforce.emp.connector.BayeuxParameters;
import com.salesforce.emp.connector.DelegatingBayeuxParameters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SalesforceAccessTokenProvider {
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	SalesforceApiService salesforceApiService;

	@Autowired
	private Gson gson;

	private static Logger logger = LoggerFactory.getLogger(SalesforceAccessTokenProvider.class);

	public BayeuxParameters updateBayeuxParametersWithSalesforceAccessToken(String sfdcHost, String username,
			String password, String consumerKey, String secret, BayeuxParameters parameters) {
		return new DelegatingBayeuxParameters(parameters) {
			@Override
			public String bearerToken() {
				String token="";
				try {
					token = salesforceApiService.getOAuthAccessToken();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return token;
			}
		};
	}

}
