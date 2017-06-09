package com.amazonaws.serverless.domain;

import java.io.Serializable;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "UserPreferences")
public class UserPreferences implements Serializable {

	private static final long serialVersionUID = -8243145429438016232L;

	@DynamoDBHashKey
	private String emailId;

	@DynamoDBRangeKey
	private String preferenceType;

	@DynamoDBAttribute
	private Set<String> categories;

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the categories
	 */
	public Set<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	/**
	 * @return the preferenceType
	 */
	public String getPreferenceType() {
		return preferenceType;
	}

	/**
	 * @param preferenceType
	 *            the preferenceType to set
	 */
	public void setPreferenceType(String preferenceType) {
		this.preferenceType = preferenceType;
	}

}
