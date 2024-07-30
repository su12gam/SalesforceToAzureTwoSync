package com.skybound.springboot.sfdcplatformevent.controller;

import com.salesforce.emp.connector.dto.LeadDto;
import com.skybound.springboot.sfdcplatformevent.service.SyncAzureToSalesforce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LeadController {

	@Autowired
	private SyncAzureToSalesforce syAzureToSalesforce;
	
	@PostMapping("/lead")
	public String getSalesforceData(@RequestBody LeadDto leadDto) {
		return syAzureToSalesforce.createLeadInDatabase(leadDto);
	}
	
	@GetMapping("/sync")
	public void syncDatabase() throws Exception {
		syAzureToSalesforce.syncDatabaseToSalesforce();
	}
}
