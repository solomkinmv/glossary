package io.github.solomkinmv.glossary.web.service;

import io.github.solomkinmv.glossary.persistence.model.User;
import io.github.solomkinmv.glossary.persistence.model.UserRole;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.github.solomkinmv.glossary.persistence.model.Role.ADMIN;
import static io.github.solomkinmv.glossary.persistence.model.Role.USER;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * Created by max on 02.01.17.
 * TODO: add JavaDoc
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public Optional<User> getByUsername(String username) {
        switch (username) {
            case "user1":
                return Optional.of(new User(1L, "user1", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G",
                        asList(new UserRole(1L, ADMIN), new UserRole(2L, USER))));
            case "user2":
                return Optional.of(new User(2L, "user2", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G",
                        singletonList(new UserRole(2L, USER))));
        }

        return Optional.empty();
    }
}
