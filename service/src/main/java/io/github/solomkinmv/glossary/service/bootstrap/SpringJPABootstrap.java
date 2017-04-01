package io.github.solomkinmv.glossary.service.bootstrap;

import io.github.solomkinmv.glossary.persistence.dao.*;
import io.github.solomkinmv.glossary.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Bootstraps data for the persistence layer.
 */
@Component
@Profile("dev")
@Transactional
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
    private Map<User, Set<WordSet>> wordSets;
    private HashMap<User, UserDictionary> userDictionaries;

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
        saveUsers();
        saveWords();
        saveStudiedWords();
        saveWordSetForUsers();
        saveUserDictionaries();
    }

    private void saveUserDictionaries() {
        User user0 = users.get(0);
        UserDictionary userDictionary0 = new UserDictionary(wordSets.get(user0), user0);

        User user1 = users.get(1);
        UserDictionary userDictionary1 = new UserDictionary(wordSets.get(user1), user1);

        userDictionaryDao.create(userDictionary0);
        userDictionaryDao.create(userDictionary1);

        userDictionaries = new HashMap<>();
        userDictionaries.put(user0, userDictionary0);
        userDictionaries.put(user1, userDictionary1);
    }

    private void saveStudiedWords() {
        List<StudiedWord> userOneStudiedWords = new ArrayList<>();
        userOneStudiedWords.add(new StudiedWord(words.get(0), WordStage.NOT_LEARNED));
        userOneStudiedWords.add(new StudiedWord(words.get(1), WordStage.NOT_LEARNED));
        userOneStudiedWords.add(new StudiedWord(words.get(2), WordStage.NOT_LEARNED));

        List<StudiedWord> userTwoStudiedWords = new ArrayList<>();
        userTwoStudiedWords.add(new StudiedWord(words.get(3), WordStage.NOT_LEARNED));
        userTwoStudiedWords.add(new StudiedWord(words.get(4), WordStage.NOT_LEARNED));
        userTwoStudiedWords.add(new StudiedWord(words.get(5), WordStage.NOT_LEARNED));
        userTwoStudiedWords.add(new StudiedWord(words.get(6), WordStage.NOT_LEARNED));

        userOneStudiedWords.forEach(studiedWordDao::create);
        userTwoStudiedWords.forEach(studiedWordDao::create);

        studiedWords = new HashMap<>();
        studiedWords.put(users.get(0), userOneStudiedWords);
        studiedWords.put(users.get(1), userTwoStudiedWords);
    }

    private void saveWordSetForUsers() {
        WordSet wordSet1 = new WordSet("Basic", "description", studiedWords.get(users.get(0)));
        WordSet wordSet2 = new WordSet("Advanced", "description", studiedWords.get(users.get(1)));

        wordSetDao.create(wordSet1);
        wordSetDao.create(wordSet2);

        wordSets = new HashMap<>();
        wordSets.put(users.get(0), Collections.singleton(wordSet1));
        wordSets.put(users.get(1), Collections.singleton(wordSet2));
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
