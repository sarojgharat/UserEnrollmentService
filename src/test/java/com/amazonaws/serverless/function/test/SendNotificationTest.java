package com.amazonaws.serverless.function.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.messagebird.MessageBirdClient;
import com.messagebird.MessageBirdService;
import com.messagebird.MessageBirdServiceImpl;
import com.messagebird.exceptions.GeneralException;
import com.messagebird.exceptions.UnauthorizedException;
import com.messagebird.objects.MessageResponse;

public class SendNotificationTest {

	public static void main(String[] args1) {
		/*
		 * if (args.length < 3) { System.out.println(
		 * "Please specify your access key, one ore more phone numbers and a message body example : java -jar <this jar file> test_accesskey 31612345678,3161112233 \"My message to be send\""
		 * ); return; }
		 */

		String key = "XxtW4Jq3oonOOhZvA55Qvnzyc";
		// First create your service object
		final MessageBirdService wsr = new MessageBirdServiceImpl(key);

		// Add the service to the client
		final MessageBirdClient messageBirdClient = new MessageBirdClient(wsr);

		String number = "+31687763866";
		try {
			// Get Hlr using msgId and msisdn
			System.out.println("Sending message:");
			final List<BigInteger> phones = new ArrayList<BigInteger>();
			for (final String phoneNumber : number.split(",")) {
				phones.add(new BigInteger(phoneNumber));
			}

			String body = "Hello JVS";
			final MessageResponse response = messageBirdClient.sendMessage("MessageBird", body, phones);
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
}