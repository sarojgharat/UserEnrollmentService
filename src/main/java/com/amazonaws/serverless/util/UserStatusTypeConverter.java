package com.amazonaws.serverless.util;

import com.amazonaws.serverless.pojo.UserStatus;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class UserStatusTypeConverter implements DynamoDBTypeConverter<String, UserStatus> {

	@Override
	public String convert(UserStatus userStatus) {
		return userStatus.toString();
	}

	@Override
	public UserStatus unconvert(String userStatus) {
		return UserStatus.valueOf(userStatus);
	}

}
