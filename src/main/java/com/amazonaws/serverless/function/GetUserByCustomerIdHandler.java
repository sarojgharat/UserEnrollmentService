package com.amazonaws.serverless.function;

import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.dao.DynamoDBUserDao;
import com.amazonaws.serverless.domain.User;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;

public class GetUserByCustomerIdHandler implements RequestHandler<User, User> {

	private static final Logger log = Logger.getLogger(GetUserByCustomerIdHandler.class);

	private static final DynamoDBUserDao userDAO = DynamoDBUserDao.instance();

	public User handleRequest(User user, Context context) {

		if (null == user || StringUtils.isNullOrEmpty(user.getCustomerId())) {
			log.error("Invalid Input:Customer ID missing from input");
			throw new IllegalArgumentException("Invalid Input:Customer ID missing from input");
		}

		List<User> userRetrievedFromBackend = userDAO.findUserByCustomerId(user.getCustomerId());

		if (userRetrievedFromBackend == null) {
			log.error("Not Found");
			throw new IllegalArgumentException("Not Found");
		}

		return userRetrievedFromBackend.get(0);
	}

}
