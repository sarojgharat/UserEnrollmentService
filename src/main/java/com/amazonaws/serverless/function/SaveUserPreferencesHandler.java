package com.amazonaws.serverless.function;

import java.util.Optional;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.dao.DynamoDBUserDao;
import com.amazonaws.serverless.dao.DynamoDBUserPreferencesDao;
import com.amazonaws.serverless.dao.UserPreferencesDao;
import com.amazonaws.serverless.domain.User;
import com.amazonaws.serverless.domain.UserPreferences;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SaveUserPreferencesHandler implements RequestHandler<UserPreferences, String> {

	private static final Logger log = Logger.getLogger(SaveUserPreferencesHandler.class);

	private static final UserPreferencesDao userPreferencesDAO = DynamoDBUserPreferencesDao.instance();

	private static final DynamoDBUserDao userDAO = DynamoDBUserDao.instance();

	public String handleRequest(UserPreferences userPreferences, Context context) {

		String response = "Failed";

		System.out.println(userPreferences);

		Optional<User> userRetrievedFromBackend = userDAO.findUserByEmailId(userPreferences.getEmailId());
		if (!userRetrievedFromBackend.isPresent()) {
			log.error("Invalid Input:No such user present");
			throw new IllegalArgumentException("Invalid Input:No such user present");
		} else {
			userPreferencesDAO.saveOrUpdateEvent(userPreferences);
			response = "Registered";
		}

		return response;
	}
}