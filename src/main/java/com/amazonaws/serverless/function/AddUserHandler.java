package com.amazonaws.serverless.function;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.dao.DynamoDBUserDao;
import com.amazonaws.serverless.domain.User;
import com.amazonaws.serverless.pojo.UserStatus;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.messagebird.MessageBirdClient;
import com.messagebird.MessageBirdService;
import com.messagebird.MessageBirdServiceImpl;
import com.messagebird.exceptions.GeneralException;
import com.messagebird.exceptions.UnauthorizedException;
import com.messagebird.objects.MessageResponse;

public class AddUserHandler implements RequestHandler<User, String> {

	private static final Logger log = Logger.getLogger(AddUserHandler.class);

	private static final DynamoDBUserDao userDAO = DynamoDBUserDao.instance();

	public String handleRequest(User user, Context context) {

		if (null == user) {
			log.error("SaveEvent received null input");
			throw new IllegalArgumentException("Cannot save null object");
		}

		Optional<User> userRetrievedFromBackend = userDAO.findUserByEmailId(user.getEmailId());
		if (userRetrievedFromBackend.isPresent()
				&& UserStatus.ENROLLMENT_ACTIVE.equals(userRetrievedFromBackend.get().getStatus())) {
			log.error("User already enrolled");
			throw new IllegalArgumentException("User already enrolled");
		}

		int activationCode = generateActivationCode();
		user.setActivationCode(activationCode);

		sendNotification(String.valueOf(user.getMobileNumber()), activationCode);

		user.setStatus(UserStatus.ENROLLMENT_PENDING);

		System.out.println("Saving or updating user with emailId = " + user.getEmailId() + " , First Name = "
				+ user.getFirstName() + " , Last Name = " + user.getLastName());

		log.info("Saving or updating user with emailId = " + user.getEmailId() + " , First Name = "
				+ user.getFirstName() + " , Last Name = " + user.getLastName());

		userDAO.saveOrUpdateEvent(user);

		return "Registered";
	}

	private void sendNotification(String number, int activationCode) {
		/*
		AmazonSNSClient snsClient = new AmazonSNSClient();
		String message = "Use this 6 digit activation code to enroll for loyalty ride: " + activationCode;
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
		sendSMSMessage(snsClient, message, phoneNumber, smsAttributes);
		*/
		String key = "XxtW4Jq3oonOOhZvA55Qvnzyc";
		// First create your service object
		final MessageBirdService wsr = new MessageBirdServiceImpl(key);
		
		// Add the service to the client
		final MessageBirdClient messageBirdClient = new MessageBirdClient(wsr);
		
		try {
			// Get Hlr using msgId and msisdn
			System.out.println("Sending message:");
			final List<BigInteger> phones = new ArrayList<BigInteger>();
			for (final String phoneNumber : number.split(",")) {
				phones.add(new BigInteger(phoneNumber));
			}

			String body = "Activation Code: " + activationCode;
			final MessageResponse response = messageBirdClient.sendMessage(
					"MessageBird", body, phones);
			System.out.println(response.toString());
		} catch (UnauthorizedException unauthorized) {
			if (unauthorized.getErrors() != null) {
				System.out.println(unauthorized.getErrors().toString());
			}
			unauthorized.printStackTrace();
		} catch (GeneralException generalException) {
			if (generalException.getErrors() != null) {
				System.out.println(generalException.getErrors().toString());
			}
			generalException.printStackTrace();
		}
		
	}

	private int generateActivationCode() {
		Random random = new Random();
		int activationCode = 100000 + random.nextInt(900000);

		System.out.println("Activation Code = " + activationCode);
		log.info("Activation Code = " + activationCode);
		return activationCode;
	}

	public void sendSMSMessage(AmazonSNSClient snsClient, String message, String phoneNumber,
			Map<String, MessageAttributeValue> smsAttributes) {
		PublishResult result = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(phoneNumber)
				.withMessageAttributes(smsAttributes));
		System.out.println(result); // Prints the message ID.
	}
}
