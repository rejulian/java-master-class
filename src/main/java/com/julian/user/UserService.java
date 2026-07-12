package com.julian.user;

import java.util.UUID;

public class UserService {

    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public User[] getUsers() {
        return userDao.getUsers();
    }

    public User getUserById(UUID id) {
        return  userDao.findUserById(id);
    }
}
