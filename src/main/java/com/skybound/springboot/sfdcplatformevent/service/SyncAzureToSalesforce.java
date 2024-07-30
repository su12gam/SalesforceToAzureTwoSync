package com.skybound.springboot.sfdcplatformevent.service;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.emp.connector.dto.LeadDto;
import com.skybound.springboot.sfdcplatformevent.SalesforceApiService;
import com.skybound.springboot.sfdcplatformevent.entity.LeadEntity;
import com.skybound.springboot.sfdcplatformevent.repository.LeadRepo;
import com.skybound.springboot.sfdcplatformevent.repository.Response;
import com.skybound.springboot.sfdcplatformevent.service.HttpService.HttpMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SyncAzureToSalesforce {

	@Autowired
	private LeadRepo leadRepo;

	@Autowired
	private SalesforceApiService salesforceApiService;

	@Autowired
	private HttpService httpService;

	@Autowired
	ObjectMapper objectMapper;

	@Value("${salesforce.instance}")
	private String salesforceInstanceUrl;

	@Transactional
	public void syncDatabaseToSalesforce() throws Exception {
		List<LeadEntity> leads = leadRepo.findAll();
		if (!leads.isEmpty()) {
			for (LeadEntity lead : leads) {
				if (!"Salesforce".equalsIgnoreCase(lead.getLastUpdatedSource())) {
					String sfId = upsertLead(lead);
					if (sfId != null && (lead.getSfid() == null || !lead.getSfid().equals(sfId))) {
						lead.setSfid(sfId);
						lead.setLastUpdatedSource("Azure");
						leadRepo.save(lead);
					}
				}
			}
		}
	}

	public String upsertLead(LeadEntity lead) throws Exception {
		if (lead.getSfid() == null || lead.getSfid().isEmpty()) {
			return createLeadInSalesforce(lead);
		} else {
			return updateLeadInSalesforce(lead);
		}
	}

	public String createLeadInSalesforce(LeadEntity lead) throws Exception {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bearer " + salesforceApiService.getOAuthAccessToken());

		try {

			HttpResponse<String> response = httpService.call(HttpMethod.POST,
					salesforceInstanceUrl + "/services/data/v59.0/sobjects/Lead", headers, createLeadData(lead));
			Response reponseObj = objectMapper.readValue(response.body(), Response.class);
			if (response.statusCode() == 200) {
				System.out.println("Lead Created Successfully in salesforce");
			} else {
				System.err.println("Failed to Create lead in salesforce: " + reponseObj.getErrors());
			}

			return reponseObj.getId();
		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		}
		return null;
	}

	private String updateLeadInSalesforce(LeadEntity lead) throws Exception {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bearer " + salesforceApiService.getOAuthAccessToken());

		try {

			HttpResponse<String> response = httpService.call(HttpMethod.POST,
					salesforceInstanceUrl + "/services/data/v59.0/sobjects/Lead" + lead.getSfid(), headers,
					createLeadData(lead));
			Response reponseObj = objectMapper.readValue(response.body(), Response.class);
			if (response.statusCode() == 200) {
				System.out.println("Lead Updated Successfully in salesforce");
			} else {
				System.err.println("Failed to update lead in salesforce: " + reponseObj.getErrors());
			}
			return reponseObj.getId();
		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		}
		return null;
	}

	public String createLeadInDatabase(LeadDto leadDto) {
		try {
			LeadEntity leadEntity = this.dtoToEntity(leadDto);
			leadEntity.setLastUpdatedSource("Azure");
			leadRepo.save(leadEntity);
			return "Lead Create In Database Successfully!!";
		} catch (Exception e) {
			// TODO: handle exception
			e.getStackTrace();
		}
		return "Something went Wrng in database.";
	}

	public String createLeadData(LeadEntity leadEntity) throws JsonProcessingException {
		LeadDto leadDto = new LeadDto();
		leadDto.setCompany(leadEntity.getCompany());
		leadDto.setEmail(leadEntity.getEmail());
		leadDto.setFirstName(leadEntity.getFirstName());
		leadDto.setLastName(leadEntity.getLastName());
		leadDto.setPhone(leadEntity.getPhone());
		leadDto.setSource("Azure");
		return objectMapper.writeValueAsString(leadDto);
	}

	public LeadEntity dtoToEntity(LeadDto leadDto) {
		LeadEntity leadEntity = new LeadEntity();
		leadEntity.setCompany(leadDto.getCompany() != null ? leadDto.getCompany() : null);
		leadEntity.setEmail(leadDto.getEmail() != null ? leadDto.getEmail() : null);
		leadEntity.setFirstName(leadDto.getFirstName() != null ? leadDto.getFirstName() : null);
		leadEntity.setLastName(leadDto.getLastName() != null ? leadDto.getLastName() : null);
		leadEntity.setPhone(leadDto.getPhone() != null ? leadDto.getPhone() : null);
		return leadEntity;
	}

}
