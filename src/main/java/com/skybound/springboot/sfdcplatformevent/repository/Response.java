package com.skybound.springboot.sfdcplatformevent.repository;

import java.util.List;

public class Response {
	private String id;
	private boolean success;
	private List<String> errors;
	private int statusCode;

	// Getters and Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String toString() {
		return "Response{" + "id='" + id + '\'' + ", success=" + success + ", errors=" + errors + ", statusCode="
				+ statusCode + '}';
	}
}
