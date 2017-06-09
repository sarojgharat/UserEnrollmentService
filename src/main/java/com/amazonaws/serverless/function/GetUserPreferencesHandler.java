package com.amazonaws.serverless.function;

import java.util.Optional;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.dao.DynamoDBUserPreferencesDao;
import com.amazonaws.serverless.dao.UserPreferencesDao;
import com.amazonaws.serverless.domain.UserPreferences;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;

public class GetUserPreferencesHandler implements RequestHandler<UserPreferences, UserPreferences> {

	private static final Logger log = Logger.getLogger(GetUserPreferencesHandler.class);

	private static final UserPreferencesDao userPreferencesDAO = DynamoDBUserPreferencesDao.instance();

	public UserPreferences handleRequest(UserPreferences userPreferencesInput, Context context) {

		if (null == userPreferencesInput || StringUtils.isNullOrEmpty(userPreferencesInput.getEmailId())) {
			log.error("Please specify emailId to retrieve user preferences");
			throw new IllegalArgumentException("Please specify emailId to retrieve user preferences");
		}

		Optional<UserPreferences> userPreferences = null;
		if (StringUtils.isNullOrEmpty(userPreferencesInput.getPreferenceType())) {
			userPreferences = userPreferencesDAO.findUserPreferencesByEmailId(userPreferencesInput.getEmailId());
		} else {
			userPreferences = userPreferencesDAO.findUserPreferencesByEmailIdAndType(userPreferencesInput.getEmailId(),
					userPreferencesInput.getPreferenceType());
		}

		if (!userPreferences.isPresent()) {
			log.error("No User Preferences saved.");
			throw new IllegalArgumentException("No User Preferences saved.");
		}

		return userPreferences.get();

	}
}