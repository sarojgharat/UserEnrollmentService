package com.amazonaws.serverless.function;

import java.util.Optional;

import org.apache.log4j.Logger;

import com.amazonaws.serverless.dao.DynamoDBUserDao;
import com.amazonaws.serverless.domain.User;
import com.amazonaws.serverless.pojo.UserStatus;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.util.StringUtils;

public class VerifyActivationCodeHandler implements RequestHandler<User, String> {

	private static final Logger log = Logger.getLogger(VerifyActivationCodeHandler.class);

	private static final DynamoDBUserDao userDAO = DynamoDBUserDao.instance();

	public String handleRequest(User user, Context context) {

		String verficationResponse = "Verification Failed";

		if (null == user || user.getActivationCode() == 0 || StringUtils.isNullOrEmpty(user.getEmailId())
				|| user.getMobileNumber() == null) {
			log.error("Invalid Input:User input should have activation code, emailId and Mobile Number");
			throw new IllegalArgumentException("Invalid Input:User input should have activation code, emailId and Mobile Number");
		}

		int activationCode = user.getActivationCode();

		Optional<User> userRetrievedFromBackend = userDAO.findUserByEmailId(user.getEmailId());
		if (!userRetrievedFromBackend.isPresent()) {
			log.error("Invalid Input:No such user present");
			throw new IllegalArgumentException("Invalid Input:No such user present");
		} else {
			User prospectiveUser = userRetrievedFromBackend.get();
			if (UserStatus.ENROLLMENT_PENDING.equals(prospectiveUser.getStatus())
					&& prospectiveUser.getActivationCode() == activationCode) {
				verficationResponse = "Verified";
				prospectiveUser.setStatus(UserStatus.ENROLLMENT_ACTIVATION_CODE_VERIFIED);
				userDAO.saveOrUpdateEvent(prospectiveUser);
			} else {
				log.error("Invalid Input:Enrollment not possible");
				throw new IllegalArgumentException("Invalid Input:Enrollment not possible");
			}
		}

		return verficationResponse;
	}

}
