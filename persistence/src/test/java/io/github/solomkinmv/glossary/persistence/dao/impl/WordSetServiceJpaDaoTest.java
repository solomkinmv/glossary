package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.dao.WordSetDao;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Test for {@link WordSetServiceJpaDao}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WordSetServiceJpaDaoTest {
    @Autowired
    private WordSetDao wordSetDao;

    @Autowired
    private WordDao wordDao;

    @Test
    public void savesWordSet() {
        String name = "name";
        String description = "description";
        WordSet wordSet = new WordSet(name, description, Collections.emptyList());

        WordSet savedWordSet = wordSetDao.update(wordSet);

        assertNotNull(savedWordSet.getId());
        assertNotNull(savedWordSet.getWords());
        assertEquals(name, savedWordSet.getName());
        assertEquals(description, savedWordSet.getDescription());
    }

    @Test
    public void savesWordSetWithNullWords() {
        String name = "name";
        String description = "description";
        WordSet wordSet = new WordSet(name, description, null);

        WordSet savedWordSet = wordSetDao.update(wordSet);

        assertNotNull(savedWordSet.getId());
        assertNull(savedWordSet.getWords());
        assertEquals(name, savedWordSet.getName());
        assertEquals(description, savedWordSet.getDescription());
    }

    @Test
    public void updatesWordSet() {
        String name = "name";
        String description = "description";
        String updatedDescription = "description 2";
        WordSet wordSet = new WordSet(name, description, Collections.emptyList());

        WordSet savedWordSet = wordSetDao.update(wordSet);
        savedWordSet.setDescription(updatedDescription);
        WordSet updatedWordSet = wordSetDao.update(savedWordSet);

        assertNotNull(updatedWordSet.getId());
        assertNotNull(updatedWordSet.getWords());
        assertEquals(name, updatedWordSet.getName());
        assertEquals(updatedDescription, updatedWordSet.getDescription());
    }

    @Test
    public void getsById() {
        String name = "name";
        String description = "description";
        WordSet wordSet = new WordSet(name, description, Collections.emptyList());

        WordSet savedWordSet = wordSetDao.update(wordSet);

        Optional<WordSet> foundWordSet = wordSetDao.findOne(savedWordSet.getId());

        assertTrue(foundWordSet.isPresent());
        assertEquals(savedWordSet, foundWordSet.orElse(null));
    }

    @Test
    public void deletesWordSet() {
        String name = "name";
        String description = "description";
        WordSet wordSet = new WordSet(name, description, Collections.emptyList());

        WordSet savedWordSet = wordSetDao.update(wordSet);
        Optional<WordSet> foundWordSet = wordSetDao.findOne(savedWordSet.getId());

        wordSetDao.delete(foundWordSet.orElse(null).getId());

        Optional<WordSet> absentWordSet = wordSetDao.findOne(savedWordSet.getId());

        assertFalse(absentWordSet.isPresent());
    }

    @Test
    public void deletesAllWithoutWords() {
        wordSetDao.update(new WordSet("text", "description", null));
        wordSetDao.update(new WordSet("text", "description", null));
        wordSetDao.update(new WordSet("text", "description", null));

        wordSetDao.deleteAll();

        assertTrue(wordSetDao.listAll().isEmpty());
    }

    @Test
    public void deletesAllWithWords() {
        List<Word> words1 = getWords();
        List<Word> words2 = getWords();
        words2.add(new Word("text", "translation"));

        wordSetDao.update(new WordSet("text", "description", words1));
        wordSetDao.update(new WordSet("text", "description", words2));

        wordSetDao.deleteAll();

        assertTrue(wordSetDao.listAll().isEmpty());
        assertEquals(words1.size() + words2.size(), wordDao.listAll().size());
    }

    @Test
    public void deletesWordFromWordSet() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        WordSet wordSet = new WordSet(name, description, words);

        WordSet savedWordSet = wordSetDao.update(wordSet);
        words = new ArrayList<>(savedWordSet.getWords()); // saved words

        Word wordToDelete = new ArrayList<>(words).get(0);
        savedWordSet.getWords().remove(wordToDelete);

        WordSet updatedWordSet = wordSetDao.update(savedWordSet);

        assertNotNull(updatedWordSet.getId());
        assertEquals(name, updatedWordSet.getName());
        assertEquals(description, updatedWordSet.getDescription());
        assertNotNull(updatedWordSet.getWords());
        assertEquals(1, updatedWordSet.getWords().size());
        assertEquals(words.size(), wordDao.listAll().size());
    }

    // delete don't delete anything. Why?
    @Ignore
    @Test(expected = PersistenceException.class)
    public void deletesWordWhichIsInWordSet() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        WordSet wordSet = new WordSet(name, description, words);

        wordSetDao.create(wordSet);
        Word wordToDelete = wordSet.getWords().get(0);

        wordDao.delete(wordToDelete.getId());
    }

    // delete don't delete anything. Why?
    @Ignore
    @Test(expected = PersistenceException.class)
    public void deletesAllWordsWhichIsInWordSet() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        WordSet wordSet = new WordSet(name, description, words);

        wordSetDao.update(wordSet);

        wordDao.deleteAll();
    }

    @Test(expected = ConstraintViolationException.class)
    public void failesToAddWordSetWithNullName() {
        wordSetDao.update(new WordSet(null, "text", Collections.emptyList()));
    }

    @Test(expected = ConstraintViolationException.class)
    public void failesToAddWordSetWithNullDescription() {
        wordSetDao.update(new WordSet("text", null, Collections.emptyList()));
    }

    @Test
    public void savesWordSetWithWords() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        WordSet wordSet = new WordSet(name, description, words);

        WordSet savedWordSet = wordSetDao.update(wordSet);

        assertNotNull(savedWordSet.getId());
        assertEquals(name, savedWordSet.getName());
        assertEquals(description, savedWordSet.getDescription());
        assertEquals(words.size(), wordDao.listAll().size());
    }

    @Test
    public void addsWordToWordSet() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        WordSet wordSet = new WordSet(name, description, words);

        WordSet savedWordSet = wordSetDao.update(wordSet);

        Word wordToAdd = new Word("added word", "added word translation");

        savedWordSet.getWords().add(wordToAdd);

        WordSet updatedWordSet = wordSetDao.update(savedWordSet);

        assertNotNull(savedWordSet.getId());
        assertEquals(name, savedWordSet.getName());
        assertEquals(description, savedWordSet.getDescription());
        assertEquals(words.size() + 1, wordDao.listAll().size());

        Word addedWord = updatedWordSet.getWords().get(words.size());

        assertNotNull(addedWord.getId());
        assertEquals("added word", addedWord.getText());
        assertEquals("added word translation", addedWord.getTranslation());
    }

    @Test
    public void assignWordsToWordSet() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        WordSet wordSet = new WordSet(name, description, Collections.emptyList());

        WordSet savedWordSet = wordSetDao.update(wordSet);
        List<Word> savedWords = words.stream().map(word -> wordDao.update(word))
                                     .collect(Collectors.toList());

        savedWordSet.getWords().addAll(savedWords);

        WordSet updatedWordSet = wordSetDao.update(savedWordSet);

        assertNotNull(updatedWordSet.getId());
        assertEquals(name, updatedWordSet.getName());
        assertEquals(description, updatedWordSet.getDescription());
        assertEquals(savedWords.size(), updatedWordSet.getWords().size());
        assertEquals(savedWords.get(0), updatedWordSet.getWords().get(0));
        assertEquals(savedWords.get(1), updatedWordSet.getWords().get(1));
    }

    private List<Word> getWords() {
        return
                new ArrayList<>(Arrays.asList(
                        new Word("word1", "translation1"),
                        new Word("word2", "translation2")
                                             ));
    }
}