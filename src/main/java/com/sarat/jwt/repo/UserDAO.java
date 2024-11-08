package com.sarat.jwt.repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.sarat.jwt.model.Role;
import com.sarat.jwt.model.User;

@Repository
public class UserDAO {

	@PersistenceContext
	private EntityManager entityManager;

	// Sample data
	private List<User> users = new ArrayList<>();

	{
//        users.add(new User(1,"janeDoe", "password456", Arrays.asList(new Role(1, "ROLE_USER"))));
//        users.add(new User( 2,"admin", "password789", Arrays.asList(new Role(2, "ROLE_ADMIN"))));
//        users.add(new User( 3,"sarat", "password123", Arrays.asList(new Role(3, "ROLE_ADMIN"),new Role(4, "ROLE_USER"))));
		users.add(new User(1, "janeDoe", "password456"));
		users.add(new User(2, "admin", "$2a$12$iG7z3U8eB3owOkDcb4zBjeI7uwIMntPNWwK22Z4hGL5KrWIQdShpO"));
		users.add(new User(3, "sarat", "$2a$12$iG7z3U8eB3owOkDcb4zBjeI7uwIMntPNWwK22Z4hGL5KrWIQdShpO"));
	}

	public Optional<User> getUserByUsername(String username) {
		return users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
	}

	public User getUserById(int id) {
		return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
	}

	public List<User> getAllUsers() {
		return users;
	}

	public void saveUser(User user) {
		users.add(user);
	}

	public void updateUser(User user) {
		int index = users.indexOf(user);
		if (index != -1) {
			users.set(index, user);
		}
	}

}