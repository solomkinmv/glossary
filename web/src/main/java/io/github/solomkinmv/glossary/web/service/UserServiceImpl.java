package io.github.solomkinmv.glossary.web.service;

import io.github.solomkinmv.glossary.persistence.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by max on 02.01.17.
 * TODO: add JavaDoc
 */
@Service
public class UserServiceImpl implements UserService {

    private final Map<String, User> users;

    public UserServiceImpl() {
        users = new HashMap<>();
        /*users.put("user1", new User(1L, "user1", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G",
                asList(new Role(1L, ADMIN), new Role(2L, USER))));
        users.put("user2", new User(2L, "user2", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G",
                singletonList(new Role(2L, USER))));*/
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }
}
