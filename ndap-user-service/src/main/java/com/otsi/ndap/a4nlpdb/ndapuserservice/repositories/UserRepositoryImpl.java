package com.otsi.ndap.a4nlpdb.ndapuserservice.repositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.otsi.ndap.a4nlpdb.ndapuserservice.dao.User;

/*
* @author Samba Siva,Chandrakanth
*/

@Repository
public class UserRepositoryImpl implements UserRepository {

	private final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);

	private CopyOnWriteArrayList<User> users;

	public UserRepositoryImpl() {
		users = new CopyOnWriteArrayList<>();
	}

	@Value("${userdata.persist.location}")
	private String userDataPath;

	@Override
	public CopyOnWriteArrayList<User> findAll() {
		return users;
	}

	@Override
	public boolean existsByUsername(String username) {
		Optional<User> optionalUser = users.stream().filter(user -> user.getUserName().equals(username)).findFirst();
		return optionalUser.isPresent();
	}

	@Override
	public User findByUsername(String username) {

		Optional<User> optionalUser = users.stream().filter(user -> user.getUserName().equals(username)).findFirst();
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		}
		return null;
	}

	@Override
	public Optional<User> finedByUserId(String userId) {
		Optional<User> optionalUser = users.stream().filter(user -> user.getUserId().equals(userId)).findFirst();
		return optionalUser;
	}

	@Override
	public User findByUsernameOrUserId(String usernameorid) {
		Optional<User> optionalUser = users.stream()
				.filter(user -> (user.getUserName().equals(usernameorid) || user.getUserId().equals(usernameorid)))
				.findFirst();
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		}

		return null;
	}

	@Override
	public User findByUsernameOrEmailOrId(String userNameorEmailorId) {
		Optional<User> optionalUser = users.stream()
				.filter(user -> (user.getUserName().equals(userNameorEmailorId)
						|| user.getEmail().equals(userNameorEmailorId) || user.getUserId().equals(userNameorEmailorId)))
				.findFirst();
		if (optionalUser.isPresent()) {
			return optionalUser.get();
		}

		return null;
	}

	@Override
	public void deleteByUsername(String username) {

		users.removeIf(user -> user.getUserName().equalsIgnoreCase(username));

	}

	@Override
	public boolean save(User user) {
		this.users.add(user);
		return true;
	}

	@Override
	public User update(User user) {
		deleteByUsername(user.getUserName());
		save(user);
		return user;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void onInit() {
		System.out.println("UsersRepositoryImpl.onInit()"+userDataPath);
		FileInputStream fi = null;
		ObjectInputStream oi = null;
		CopyOnWriteArrayList<User> users = null;
		try {
			File file = new File(userDataPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			if (file.length() > 0) {
				fi = new FileInputStream(file);
				oi = new ObjectInputStream(fi);
				users = (CopyOnWriteArrayList<User>) oi.readObject();
			}
			if (users == null || users.isEmpty()) {
				this.users = new CopyOnWriteArrayList<>();
			} else {
				this.users = users;
			}

		} catch (Exception e) {
			// e.printStackTrace();
			log.info("UserRepositoryImpl.onInit() no data available." + e.getMessage());
		} finally {
			if (fi != null) {
				try {
					fi.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (oi != null) {
				try {
					oi.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@PreDestroy
	public void onDestroy() {
		System.out.println("UsersRepositoryImpl.onDestroy()");
		ObjectOutputStream o = null;
		FileOutputStream f = null;
		try {
			f = new FileOutputStream(new File(userDataPath));
			o = new ObjectOutputStream(f);
			o.writeObject(users);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (o != null) {
				try {
					o.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (f != null) {
				try {
					f.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
