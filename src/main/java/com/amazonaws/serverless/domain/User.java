package com.amazonaws.serverless.domain;

import com.amazonaws.serverless.pojo.UserStatus;
import com.amazonaws.serverless.util.UserStatusTypeConverter;
import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.io.Serializable;

@DynamoDBTable(tableName = "Users")
public class User implements Serializable {

    private static final long serialVersionUID = -8243145429438016232L;
    public static final String CUSTOMER_ID_INDEX = "CustomerId-Index";

    @DynamoDBHashKey
    private String emailId;
    
    @DynamoDBIndexHashKey(globalSecondaryIndexName = CUSTOMER_ID_INDEX)
    private String customerId;
    
	@DynamoDBAttribute
    private String iban;
	
    @DynamoDBAttribute
    private String firstName;
    
    @DynamoDBAttribute
    private String lastName;
    
    @DynamoDBAttribute
    private Long mobileNumber;
    
    @DynamoDBAttribute
    private int activationCode;
    
    @DynamoDBTypeConverted(converter = UserStatusTypeConverter.class)
    @DynamoDBAttribute
    private UserStatus status;

    public User() { }
    
    /**
	 * @param emailId
	 * @param firstName
	 * @param lastName
	 * @param mobileNumber
	 * @param iban
	 * @param customerId
	 * @param activationCode
	 * @param status
	 */
	public User(String emailId, String firstName, String lastName, Long mobileNumber, String iban, String customerId,
			Integer activationCode, UserStatus status) {
		super();
		this.emailId = emailId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.iban = iban;
		this.customerId = customerId;
		this.activationCode = activationCode;
		this.status = status;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the mobileNumber
	 */
	public Long getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the iban
	 */
	public String getIban() {
		return iban;
	}

	/**
	 * @param iban the iban to set
	 */
	public void setIban(String iban) {
		this.iban = iban;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the activationCode
	 */
	public int getActivationCode() {
		return activationCode;
	}

	/**
	 * @param activationCode the activationCode to set
	 */
	public void setActivationCode(int activationCode) {
		this.activationCode = activationCode;
	}

	/**
	 * @return the status
	 */
	public UserStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	
	

	
}
