package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.TopicDao;
import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.Topic;
import io.github.solomkinmv.glossary.persistence.model.Word;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Test for {@link TopicServiceJpaDao}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
// FIXME: add @After method to delete all records from WORD and TOPIC
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TopicServiceJpaDaoTest {
    @Autowired
    private TopicDao topicDao;

    @Autowired
    private WordDao wordDao;

    @Test
    public void savesTopic() {
        String name = "name";
        String description = "description";
        Topic topic = new Topic(name, description, Collections.emptyList());

        Topic savedTopic = topicDao.saveOrUpdate(topic);

        assertNotNull(savedTopic.getId());
        assertNotNull(savedTopic.getWords());
        assertEquals(name, savedTopic.getName());
        assertEquals(description, savedTopic.getDescription());
    }

    @Test
    public void savesTopicWithNullWords() {
        String name = "name";
        String description = "description";
        Topic topic = new Topic(name, description, null);

        Topic savedTopic = topicDao.saveOrUpdate(topic);

        assertNotNull(savedTopic.getId());
        assertNull(savedTopic.getWords());
        assertEquals(name, savedTopic.getName());
        assertEquals(description, savedTopic.getDescription());
    }

    @Test
    public void updatesTopic() {
        String name = "name";
        String description = "description";
        String updatedDescription = "description 2";
        Topic topic = new Topic(name, description, Collections.emptyList());

        Topic savedTopic = topicDao.saveOrUpdate(topic);
        savedTopic.setDescription(updatedDescription);
        Topic updatedTopic = topicDao.saveOrUpdate(savedTopic);

        assertNotNull(updatedTopic.getId());
        assertNotNull(updatedTopic.getWords());
        assertEquals(name, updatedTopic.getName());
        assertEquals(updatedDescription, updatedTopic.getDescription());
    }

    @Test
    public void getsById() {
        String name = "name";
        String description = "description";
        Topic topic = new Topic(name, description, Collections.emptyList());

        Topic savedTopic = topicDao.saveOrUpdate(topic);

        Optional<Topic> foundTopic = topicDao.getById(savedTopic.getId());

        assertTrue(foundTopic.isPresent());
        assertEquals(savedTopic, foundTopic.orElse(null));
    }

    @Test
    public void deletesTopic() {
        String name = "name";
        String description = "description";
        Topic topic = new Topic(name, description, Collections.emptyList());

        Topic savedTopic = topicDao.saveOrUpdate(topic);
        Optional<Topic> foundTopic = topicDao.getById(savedTopic.getId());

        topicDao.delete(foundTopic.orElse(null).getId());

        Optional<Topic> absentTopic = topicDao.getById(savedTopic.getId());

        assertFalse(absentTopic.isPresent());
    }

    @Test
    public void deletesAllWithoutWords() {
        topicDao.saveOrUpdate(new Topic("text", "description", null));
        topicDao.saveOrUpdate(new Topic("text", "description", null));
        topicDao.saveOrUpdate(new Topic("text", "description", null));

        topicDao.deleteAll();

        assertTrue(topicDao.listAll().isEmpty());
    }

    @Test
    public void deletesAllWithWords() {
        List<Word> words1 = getWords();
        List<Word> words2 = getWords();
        words2.add(new Word("text", "translation"));

        topicDao.saveOrUpdate(new Topic("text", "description", words1));
        topicDao.saveOrUpdate(new Topic("text", "description", words2));

        topicDao.deleteAll();

        assertTrue(topicDao.listAll().isEmpty());
        assertEquals(words1.size() + words2.size(), wordDao.listAll().size());
    }

    @Test
    public void deletesWordFromTopic() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        Topic topic = new Topic(name, description, words);

        Topic savedTopic = topicDao.saveOrUpdate(topic);
        words = new ArrayList<>(savedTopic.getWords()); // saved words

        Word wordToDelete = new ArrayList<>(words).get(0);
        savedTopic.getWords().remove(wordToDelete);

        Topic updatedTopic = topicDao.saveOrUpdate(savedTopic);

        assertNotNull(updatedTopic.getId());
        assertEquals(name, updatedTopic.getName());
        assertEquals(description, updatedTopic.getDescription());
        assertNotNull(updatedTopic.getWords());
        assertEquals(1, updatedTopic.getWords().size());
        assertEquals(words.size(), wordDao.listAll().size());
    }

    // Is it correct behaviour?
    // Maybe I should trigger remove from child to parent?
    @Test(expected = PersistenceException.class)
    public void deletesWordWhichIsInTopic() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        Topic topic = new Topic(name, description, words);

        Topic savedTopic = topicDao.saveOrUpdate(topic);
        words = new ArrayList<>(savedTopic.getWords()); // saved words
        Word wordToDelete = new ArrayList<>(words).get(0);

        wordDao.delete(wordToDelete.getId());
    }

    // Is it correct behaviour?
    // Maybe I should trigger remove from child to parent?
    @Test(expected = PersistenceException.class)
    public void deletesAllWordsWhichIsInTopic() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        Topic topic = new Topic(name, description, words);

        topicDao.saveOrUpdate(topic);

        wordDao.deleteAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public void failesToAddNullTopic() {
        topicDao.saveOrUpdate(null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void failesToAddTopicWithNullName() {
        topicDao.saveOrUpdate(new Topic(null, "text", Collections.emptyList()));
    }

    @Test(expected = ConstraintViolationException.class)
    public void failesToAddTopicWithNullDescription() {
        topicDao.saveOrUpdate(new Topic("text", null, Collections.emptyList()));
    }

    @Test
    public void savesTopicWithWords() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        Topic topic = new Topic(name, description, words);

        Topic savedTopic = topicDao.saveOrUpdate(topic);

        assertNotNull(savedTopic.getId());
        assertEquals(name, savedTopic.getName());
        assertEquals(description, savedTopic.getDescription());
        assertEquals(words.size(), wordDao.listAll().size());
    }

    @Test
    public void addsWordToTopic() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        Topic topic = new Topic(name, description, words);

        Topic savedTopic = topicDao.saveOrUpdate(topic);

        Word wordToAdd = new Word("added word", "added word translation");

        savedTopic.getWords().add(wordToAdd);

        Topic updatedTopic = topicDao.saveOrUpdate(savedTopic);

        assertNotNull(savedTopic.getId());
        assertEquals(name, savedTopic.getName());
        assertEquals(description, savedTopic.getDescription());
        assertEquals(words.size() + 1, wordDao.listAll().size());

        Word addedWord = updatedTopic.getWords().get(words.size());

        assertNotNull(addedWord.getId());
        assertEquals("added word", addedWord.getText());
        assertEquals("added word translation", addedWord.getTranslation());
    }

    @Test
    public void assignWordsToTopic() {
        List<Word> words = getWords();

        String name = "name";
        String description = "description";
        Topic topic = new Topic(name, description, Collections.emptyList());

        Topic savedTopic = topicDao.saveOrUpdate(topic);
        List<Word> savedWords = words.stream().map(word -> wordDao.saveOrUpdate(word))
                                     .collect(Collectors.toList());

        savedTopic.getWords().addAll(savedWords);

        Topic updatedTopic = topicDao.saveOrUpdate(savedTopic);

        assertNotNull(updatedTopic.getId());
        assertEquals(name, updatedTopic.getName());
        assertEquals(description, updatedTopic.getDescription());
        assertEquals(savedWords.size(), updatedTopic.getWords().size());
        assertEquals(savedWords.get(0), updatedTopic.getWords().get(0));
        assertEquals(savedWords.get(1), updatedTopic.getWords().get(1));
    }

    private List<Word> getWords() {
        return
                new ArrayList<>(Arrays.asList(
                        new Word("word1", "translation1"),
                        new Word("word2", "translation2")
                                             ));
    }
}