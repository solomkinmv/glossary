package io.github.solomkinmv.glossary.persistence.bootstrap;

import io.github.solomkinmv.glossary.persistence.dao.*;
import io.github.solomkinmv.glossary.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bootstraps data for the persistence layer.
 */
@Component
@Profile("dev")
public class SpringJPABootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final WordDao wordDao;
    private final WordSetDao wordSetDao;
    private final UserDictionaryDao userDictionaryDao;

    @Autowired
    public SpringJPABootstrap(UserDao userDao, RoleDao roleDao, WordDao wordDao, WordSetDao wordSetDao,
                              UserDictionaryDao userDictionaryDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.wordDao = wordDao;
        this.wordSetDao = wordSetDao;
        this.userDictionaryDao = userDictionaryDao;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Role adminRole = roleDao.update(new Role(RoleType.ADMIN));
        Role userRole = roleDao.update(new Role(RoleType.USER));

        User user1 = userDao.update(
                new User("user1", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G", "user1@email.com",
                        Collections.singletonList(userRole)));
        User user2 = userDao.update(
                new User("user2", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G", "user2@email.com",
                        Collections.singletonList(userRole)));

        List<Word> savedWords = saveWords();

        WordSet wordSet = wordSetDao.update(new WordSet("Basic", "description", savedWords));

        UserDictionary userDictionary = userDictionaryDao.update(
                new UserDictionary(new HashSet<>(Collections.singletonList(wordSet)), user1));

        System.out.println();
    }

    private List<Word> saveWords() {
        ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("user", "пользователь"));
        words.add(new Word("glass", "стекло"));
        words.add(new Word("potato", "картошка"));
        words.add(new Word("teapot", "чайник"));
        words.add(new Word("tomato", "помидор"));
        words.add(new Word("pocket", "карман"));
        words.add(new Word("pen", "ручка"));

        return words.stream()
                    .map(wordDao::update)
                    .collect(Collectors.toList());
    }
}
