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
import java.util.List;
import java.util.Map;

/**
 * Bootstraps data for the persistence layer.
 */
@Component
@Profile("dev")
public class SpringJPABootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final WordDao wordDao;
    private final StudiedWordDao studiedWordDao;
    private final WordSetDao wordSetDao;
    private final UserDictionaryDao userDictionaryDao;

    private List<Word> words;
    private Role adminRole;
    private Role userRole;
    private List<User> users;
    private Map<User, List<StudiedWord>> studiedWords;

    @Autowired
    public SpringJPABootstrap(UserDao userDao, RoleDao roleDao, WordDao wordDao, StudiedWordDao studiedWordDao,
                              WordSetDao wordSetDao,
                              UserDictionaryDao userDictionaryDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.wordDao = wordDao;
        this.studiedWordDao = studiedWordDao;
        this.wordSetDao = wordSetDao;
        this.userDictionaryDao = userDictionaryDao;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        saveRoles();
        saveWords();
        saveUsers();
        saveStudiedWords();
        saveWordSetForUsers();
    }

    private void saveStudiedWords() {
        List<StudiedWord> userOneStudiedWords = new ArrayList<>();
        userOneStudiedWords.add(new StudiedWord(words.get(0), WordStage.NOT_STUDIED));
        userOneStudiedWords.add(new StudiedWord(words.get(1), WordStage.NOT_STUDIED));
        userOneStudiedWords.add(new StudiedWord(words.get(2), WordStage.NOT_STUDIED));

        List<StudiedWord> userTwoStudiedWords = new ArrayList<>();
        userTwoStudiedWords.add(new StudiedWord(words.get(3), WordStage.NOT_STUDIED));
        userTwoStudiedWords.add(new StudiedWord(words.get(4), WordStage.NOT_STUDIED));
        userTwoStudiedWords.add(new StudiedWord(words.get(5), WordStage.NOT_STUDIED));
        userTwoStudiedWords.add(new StudiedWord(words.get(6), WordStage.NOT_STUDIED));

        userOneStudiedWords.forEach(studiedWordDao::create);
        userTwoStudiedWords.forEach(studiedWordDao::create);

        studiedWords.put(users.get(0), userOneStudiedWords);
        studiedWords.put(users.get(1), userTwoStudiedWords);
    }

    private void saveWordSetForUsers() {
        WordSet wordSet1 = new WordSet("Basic", "description", studiedWords.get(users.get(0)));
        WordSet wordSet2 = new WordSet("Advanced", "description", studiedWords.get(users.get(1)));

        wordSetDao.create(wordSet1);
        wordSetDao.create(wordSet2);
    }

    private void saveUsers() {
        users = new ArrayList<>();
        users.add(new User("user1", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G", "user1@email.com",
                Collections.singletonList(userRole)));
        users.add(new User("user2", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G", "user2@email.com",
                Collections.singletonList(userRole)));

        users.forEach(userDao::create);
    }

    private void saveRoles() {
        adminRole = new Role(RoleType.ADMIN);
        userRole = new Role(RoleType.USER);

        roleDao.create(adminRole);
        roleDao.create(userRole);
    }

    private void saveWords() {
        words = new ArrayList<>();
        words.add(new Word("user", "пользователь"));
        words.add(new Word("glass", "стекло"));
        words.add(new Word("potato", "картошка"));
        words.add(new Word("teapot", "чайник"));
        words.add(new Word("tomato", "помидор"));
        words.add(new Word("pocket", "карман"));
        words.add(new Word("pen", "ручка"));

        words.forEach(wordDao::create);
    }
}
