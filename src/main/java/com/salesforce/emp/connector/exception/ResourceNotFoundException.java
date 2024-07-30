package com.salesforce.emp.connector.exception;

public class ResourceNotFoundException extends RuntimeException {

	String resouceName;
	String fieldName;
	long fieldValue;

	public ResourceNotFoundException(String resouceName, String fieldName, long fieldValue) {
		super(String.format(resouceName + "not found with" + fieldName + " : " + fieldValue));
		this.resouceName = resouceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getResouceName() {
		return resouceName;
	}

	public void setResouceName(String resouceName) {
		this.resouceName = resouceName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public long getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(long fieldValue) {
		this.fieldValue = fieldValue;
	}

}