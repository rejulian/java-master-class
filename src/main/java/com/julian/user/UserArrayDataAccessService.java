package com.julian.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserArrayDataAccessService implements UserDao{
    private static final List<User> USERS = new ArrayList<>();

    static {
        USERS.add(new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "Julian"));
        USERS.add(new User(UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"), "Nelson"));
    }

    @Override
    public List<User> getUsers() {
        return USERS;
    }

    @Override
    public User findUserById(UUID id) {
        for (User user : USERS) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

}
