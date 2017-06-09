
package com.amazonaws.serverless.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.domain.User;
import com.amazonaws.serverless.manager.DynamoDBManager;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class DynamoDBUserDao implements UserDao {

	private static final Logger log = Logger.getLogger(DynamoDBUserDao.class);

	private static final DynamoDBMapper mapper = DynamoDBManager.mapper();

	private static volatile DynamoDBUserDao instance;

	private DynamoDBUserDao() {
	}

	public static DynamoDBUserDao instance() {

		if (instance == null) {
			synchronized (DynamoDBUserDao.class) {
				if (instance == null)
					instance = new DynamoDBUserDao();
			}
		}
		return instance;
	}

	@Override
	public void saveOrUpdateEvent(User user) {
		System.out.println("Inside saveOrUpdateEvent of DAO");
		mapper.save(user);
		System.out.println("After saveOrUpdateEvent of DAO");
	}

	@Override
	public Optional<User> findUserByEmailId(String emailId) {

		DynamoDBQueryExpression<User> userQuery = new DynamoDBQueryExpression<>();

		User user = new User();
		user.setEmailId(emailId);

		userQuery.setHashKeyValues(user);
		List<User> users = mapper.query(User.class, userQuery);
		
		System.out.println(users);
		
		System.out.println(users.size());

		return Optional.ofNullable(users.size() != 0 ? users.get(0) : null);
	}
	
	public List<User> findUserByCustomerId(String customerId) {
		Map<String, AttributeValue> eav = new HashMap<>();
		eav.put(":v1", new AttributeValue().withS(customerId));
	
		DynamoDBQueryExpression<User> query = new DynamoDBQueryExpression<User>()
				.withIndexName(User.CUSTOMER_ID_INDEX).withConsistentRead(false)
				.withKeyConditionExpression("customerId = :v1").withExpressionAttributeValues(eav);
	
		return mapper.query(User.class, query);
	}

}
