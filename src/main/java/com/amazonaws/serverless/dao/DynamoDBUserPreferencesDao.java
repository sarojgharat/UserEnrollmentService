
package com.amazonaws.serverless.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.domain.UserPreferences;
import com.amazonaws.serverless.manager.DynamoDBManager;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;

public class DynamoDBUserPreferencesDao implements UserPreferencesDao {

	private static final Logger log = Logger.getLogger(DynamoDBUserPreferencesDao.class);

	private static final DynamoDBMapper mapper = DynamoDBManager.mapper();

	private static volatile DynamoDBUserPreferencesDao instance;

	private DynamoDBUserPreferencesDao() {
	}

	public static DynamoDBUserPreferencesDao instance() {

		if (instance == null) {
			synchronized (DynamoDBUserPreferencesDao.class) {
				if (instance == null)
					instance = new DynamoDBUserPreferencesDao();
			}
		}
		return instance;
	}

	@Override
	public void saveOrUpdateEvent(UserPreferences userPreferences) {
		System.out.println("Inside saveOrUpdateEvent of DAO");
		mapper.save(userPreferences);
		System.out.println("After saveOrUpdateEvent of DAO");
	}

	@Override
	public Optional<UserPreferences> findUserPreferencesByEmailId(String emailId) {

		DynamoDBQueryExpression<UserPreferences> userPreferencesQuery = new DynamoDBQueryExpression<>();

		UserPreferences inputUserPreferences = new UserPreferences();
		inputUserPreferences.setEmailId(emailId);

		userPreferencesQuery.setHashKeyValues(inputUserPreferences);

		List<UserPreferences> userPreferences = mapper.query(UserPreferences.class, userPreferencesQuery);

		return Optional.ofNullable(userPreferences.size() != 0 ? userPreferences.get(0) : null);
	}

	@Override
	public Optional<UserPreferences> findUserPreferencesByEmailIdAndType(String emailId, String preferenceType) {
		DynamoDBQueryExpression<UserPreferences> userPreferencesQuery = new DynamoDBQueryExpression<>();

		UserPreferences inputUserPreferences = new UserPreferences();
		inputUserPreferences.setEmailId(emailId);

		Condition rangeKeyCondition = new Condition().withComparisonOperator(ComparisonOperator.EQ.toString())
				.withAttributeValueList(new AttributeValue().withS(preferenceType));

		Map<String, Condition> rangeKeyConditionMap = new HashMap<>();
		rangeKeyConditionMap.put("preferenceType", rangeKeyCondition);

		userPreferencesQuery.setHashKeyValues(inputUserPreferences);
		userPreferencesQuery.setRangeKeyConditions(rangeKeyConditionMap);

		List<UserPreferences> userPreferences = mapper.query(UserPreferences.class, userPreferencesQuery);

		return Optional.ofNullable(userPreferences.size() != 0 ? userPreferences.get(0) : null);
	}

}
