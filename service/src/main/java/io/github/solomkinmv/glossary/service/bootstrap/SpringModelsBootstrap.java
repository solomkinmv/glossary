package io.github.solomkinmv.glossary.service.bootstrap;

import io.github.solomkinmv.glossary.persistence.dao.RoleDao;
import io.github.solomkinmv.glossary.persistence.model.*;
import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import io.github.solomkinmv.glossary.service.domain.UserService;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Supplier;

/**
 * Bootstraps data for the persistence layer.
 */
@Component
@Profile("dev")
@Transactional
@Slf4j
public class SpringModelsBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserService userService;
    private final RoleDao roleDao;
    private final WordService wordService;
    private final WordSetService wordSetService;
    private final UserDictionaryService userDictionaryService;

    private Map<RoleType, Role> roles;
    private List<User> users;
    private Map<User, List<StudiedWord>> studiedWords;
    private Map<User, Set<WordSet>> wordSets;
    private HashMap<User, UserDictionary> userDictionaries;

    @Autowired
    public SpringModelsBootstrap(UserService userService,
                                 RoleDao roleDao, WordService wordService,
                                 WordSetService wordSetService,
                                 UserDictionaryService userDictionaryService) {
        this.userService = userService;
        this.roleDao = roleDao;
        this.wordService = wordService;
        this.wordSetService = wordSetService;
        this.userDictionaryService = userDictionaryService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Bootstrapping application for dev environment");
        if (isDbBootstrapped()) {
            return;
        }
        saveRoles();
        saveUsers();
        saveStudiedWords();
        saveWordSetForUsers();
        saveUserDictionaries();
    }

    private void saveUserDictionaries() {
        User user0 = users.get(0);
        UserDictionary userDictionary0 = new UserDictionary(wordSets.get(user0), user0);

        User user1 = users.get(1);
        UserDictionary userDictionary1 = new UserDictionary(wordSets.get(user1), user1);

        userDictionaryService.save(userDictionary0);
        userDictionaryService.save(userDictionary1);

        userDictionaries = new HashMap<>();
        userDictionaries.put(user0, userDictionary0);
        userDictionaries.put(user1, userDictionary1);
    }

    private void saveStudiedWords() {
        List<StudiedWord> userOneStudiedWords = new ArrayList<>();
        userOneStudiedWords.add(new StudiedWord("user", "пользователь"));
        userOneStudiedWords.add(new StudiedWord("glass", "стекло"));
        userOneStudiedWords.add(new StudiedWord("potato", "картошка"));

        List<StudiedWord> userTwoStudiedWords = new ArrayList<>();
        userTwoStudiedWords.add(new StudiedWord("teapot", "чайник"));
        userTwoStudiedWords.add(new StudiedWord("tomato", "помидор"));
        userTwoStudiedWords.add(new StudiedWord("pocket", "карман"));
        userTwoStudiedWords.add(new StudiedWord("pen", "ручка"));

        userOneStudiedWords.forEach(wordService::save);
        userTwoStudiedWords.forEach(wordService::save);

        studiedWords = new HashMap<>();
        studiedWords.put(users.get(0), userOneStudiedWords);
        studiedWords.put(users.get(1), userTwoStudiedWords);
    }

    private void saveWordSetForUsers() {
        WordSet wordSet1 = new WordSet("Basic", "description", studiedWords.get(users.get(0)));
        WordSet wordSet2 = new WordSet("Advanced", "description", studiedWords.get(users.get(1)));

        wordSetService.save(wordSet1);
        wordSetService.save(wordSet2);

        wordSets = new HashMap<>();
        wordSets.put(users.get(0), Collections.singleton(wordSet1));
        wordSets.put(users.get(1), Collections.singleton(wordSet2));
    }

    private void saveUsers() {
        users = new ArrayList<>();
        users.add(new User("John", "user1", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G",
                           "user1@email.com",
                           Collections.singleton(roles.get(RoleType.USER))));
        users.add(new User("Charles", "user2", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G",
                           "user2@email.com",
                           Collections.singleton(roles.get(RoleType.USER))));

        users.forEach(userService::save);
    }

    private void saveRoles() {
        roles = new HashMap<>();
        for (RoleType roleType : RoleType.values()) {
            ensureRole(roleType);
        }
    }

    private void ensureRole(RoleType roleType) {
        roles.put(roleType, roleDao.findByRoleType(roleType).orElseGet(saveRole(roleType)));
    }

    private Supplier<Role> saveRole(RoleType roleType) {
        return () -> {
            Role role = new Role(roleType);
            roleDao.create(role);
            return role;
        };
    }

    private boolean isDbBootstrapped() {
        return roleDao.findByRoleType(RoleType.USER).isPresent();
    }
}
