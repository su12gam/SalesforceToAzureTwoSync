package com.salesforce.emp.connector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeadDto {

	@JsonProperty("Company")
	private String company;

	@JsonProperty("Email")
	private String email;

	@JsonProperty("Phone")
	private String phone;

	@JsonProperty("FirstName")
	private String firstName;

	@JsonProperty("LastName")
	private String lastName;

	@JsonProperty("Source__c")
	private String source;

	public LeadDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LeadDto(String company, String email, String phone, String firstName, String lastName, String source) {
		super();
		this.company = company;
		this.email = email;
		this.phone = phone;
		this.firstName = firstName;
		this.lastName = lastName;
		this.source = source;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
