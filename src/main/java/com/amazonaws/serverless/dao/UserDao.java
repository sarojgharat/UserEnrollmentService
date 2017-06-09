package com.amazonaws.serverless.dao;

import java.util.Optional;

import com.amazonaws.serverless.domain.User;

public interface UserDao {

	void saveOrUpdateEvent(User user);

	Optional<User> findUserByEmailId(String emailId);

}
