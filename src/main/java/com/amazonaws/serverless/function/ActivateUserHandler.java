package com.amazonaws.serverless.function;

import java.util.Optional;
import java.util.Random;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.dao.DynamoDBUserDao;
import com.amazonaws.serverless.domain.User;
import com.amazonaws.serverless.pojo.UserStatus;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;

public class ActivateUserHandler implements RequestHandler<User, User> {

	private static final Logger log = Logger.getLogger(ActivateUserHandler.class);

	private static final DynamoDBUserDao userDAO = DynamoDBUserDao.instance();

	public User handleRequest(User user, Context context) {

		if (null == user || StringUtils.isNullOrEmpty(user.getIban())) {
			log.error("Invalid Input:Iban should be part of input.");
			throw new IllegalArgumentException("Invalid Input:Iban should be part of input.");
		}

		Optional<User> userRetrievedFromBackend = userDAO.findUserByEmailId(user.getEmailId());
		
		if (!userRetrievedFromBackend.isPresent()) {
			log.error("Invalid Input:User not present");
			throw new IllegalArgumentException("Invalid Input:User not present");
		}
		
		if (userRetrievedFromBackend.isPresent()
				&& UserStatus.ENROLLMENT_ACTIVE.equals(userRetrievedFromBackend.get().getStatus())) {
			log.error("Invalid Input:User already active");
			throw new IllegalArgumentException("Invalid Input:User already active");
		}
		
		
		if (userRetrievedFromBackend.isPresent()
				&& !UserStatus.ENROLLMENT_ACTIVATION_CODE_VERIFIED.equals(userRetrievedFromBackend.get().getStatus())) {
			log.error("Invalid Input:Activation code not verified for user.");
			throw new IllegalArgumentException("Invalid Input:Activation code not verified for user.");
		}

		User enrolledUser = userRetrievedFromBackend.get();

		enrolledUser.setIban(user.getIban());
		enrolledUser.setStatus(UserStatus.ENROLLMENT_ACTIVE);
		enrolledUser.setCustomerId(generateCustomerId());

		System.out.println("Activated user with emailId = " + enrolledUser.getEmailId());

		log.info("Activated user with emailId = " + enrolledUser.getEmailId());

		userDAO.saveOrUpdateEvent(enrolledUser);

		return enrolledUser;
	}

	private String generateCustomerId() {
		Random random = new Random();
		int customerId = 10000000 + random.nextInt(90000000);

		System.out.println("Customer Id = " + customerId);
		log.info("Customer Id = " + customerId);
		return String.valueOf(customerId);
	}

}
