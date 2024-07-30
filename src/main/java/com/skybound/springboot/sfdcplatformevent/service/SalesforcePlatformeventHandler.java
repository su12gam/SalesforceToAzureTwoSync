package com.skybound.springboot.sfdcplatformevent.service;

import java.util.Map;

import com.google.gson.Gson;
import com.skybound.springboot.sfdcplatformevent.entity.LeadEntity;
import com.skybound.springboot.sfdcplatformevent.repository.LeadRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SalesforcePlatformeventHandler {
	private static Logger logger = LoggerFactory.getLogger(SalesforcePlatformeventHandler.class);

	@Autowired
	LeadRepo leadRepo;

	public void handleRequest(String payload, String salesforceEventName, Long replayId) {
		logger.info("Received payload from platform event " + salesforceEventName + " with replayId: " + replayId);
		logger.info(payload);

		Map<String, Object> mapValues = jsonToMap(payload);

		if (!"Azure".equalsIgnoreCase(String.valueOf(mapValues.get("Source__c")))) {
			if (String.valueOf(mapValues.get("ChangeType__c")).equalsIgnoreCase("Create")
					|| String.valueOf(mapValues.get("ChangeType__c")).equalsIgnoreCase("Update")) {
				LeadEntity leadEntity = leadRepo.findBySfId(String.valueOf(mapValues.get("RecordId__c")),
						String.valueOf(mapValues.get("FirstName__c")),String.valueOf(mapValues.get("LastName__c")));

				if (leadEntity == null) {
					leadEntity = new LeadEntity();
				}
				leadEntity.setChangeType(String.valueOf(mapValues.get("ChangeType__c")));
				leadEntity.setCompany(String.valueOf(mapValues.get("Company__c")));
				leadEntity.setEmail(String.valueOf(mapValues.get("Email__c")));
				leadEntity.setFirstName(String.valueOf(mapValues.get("FirstName__c")));
				leadEntity.setLastName(String.valueOf(mapValues.get("LastName__c")));
				leadEntity.setPhone(String.valueOf(mapValues.get("Phone__c")));
				leadEntity.setSfid(String.valueOf(mapValues.get("RecordId__c")));
				leadEntity.setLastUpdatedSource("Salesforce");
				leadRepo.save(leadEntity);
			} else if (String.valueOf(mapValues.get("ChangeType__c")).equalsIgnoreCase("Delete")) {
				LeadEntity leadEntity = leadRepo.findBySfid(String.valueOf(mapValues.get("RecordId__c")));
				if (leadEntity != null) {
					leadRepo.delete(leadEntity);
				}
//				new com.salesforce.emp.connector.exception.ResourceNotFoundException("user", "id", (Long)mapValues.get("RrcordId__c"))
			}
		}
	}

	public static Map<String, Object> jsonToMap(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Map.class);
	}
}
