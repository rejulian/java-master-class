package com.julian.user;

import java.util.UUID;

public class UserDao {
    private static final User[] USERS;

    static {
        USERS = new User[]{
                new User(UUID.fromString("8ca51d2b-aaaf-4bf2-834a-e02964e10fc3"), "Julian"),
                new User(UUID.fromString("b10d126a-3608-4980-9f9c-aa179f5cebc3"), "Nelson")
        };
    }

    public User[] getUsers() {
        return USERS;
    }

    public User findUserById(UUID id) {
        for (User user : USERS) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

}
