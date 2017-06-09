package com.amazonaws.serverless.dao;

import java.util.Optional;

import com.amazonaws.serverless.domain.UserPreferences;

public interface UserPreferencesDao {

	void saveOrUpdateEvent(UserPreferences userPreferences);

	Optional<UserPreferences> findUserPreferencesByEmailId(String emailId);

	Optional<UserPreferences> findUserPreferencesByEmailIdAndType(String emailId, String preferenceType);

}
