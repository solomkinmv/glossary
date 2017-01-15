package io.github.solomkinmv.glossary.stub;

import io.github.solomkinmv.glossary.persistence.dao.UserDao;
import io.github.solomkinmv.glossary.persistence.model.User;

import java.util.*;

/**
 * Stub implementation of {@link UserDao}. Only for test purposes.
 */
public class UserDaoStub implements UserDao {

    private List<User> users;

    public UserDaoStub() {
        users = new ArrayList<>();
        users.add(new User("user1", "pass1", "user1@email.com", null));
        users.add(new User("user2", "pass2", "user2@email.com", null));
    }

    @Override
    public List<User> listAll() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public Optional<User> getById(Long id) {
        return users.stream()
                    .filter(user -> Objects.equals(user.getId(), id))
                    .findFirst();
    }

    @Override
    public User saveOrUpdate(User user) {
        if (user.getId() == null) {
            long maxId = users.stream()
                              .mapToLong(User::getId)
                              .max()
                              .orElseGet(() -> 0L);
            user.setId(maxId + 1);
        }

        users.add(user);

        return user;
    }

    @Override
    public void delete(Long id) {
        users.removeIf(user -> Objects.equals(user.getId(), id));
    }

    @Override
    public void deleteAll() {
        users = new ArrayList<>();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst();
    }
}
