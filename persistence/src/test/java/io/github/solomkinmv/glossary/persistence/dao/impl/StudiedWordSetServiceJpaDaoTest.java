//package io.github.solomkinmv.glossary.persistence.dao.impl;
//
//import io.github.solomkinmv.glossary.persistence.dao.WordDao;
//import io.github.solomkinmv.glossary.persistence.dao.WordSetDao;
//import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
//import io.github.solomkinmv.glossary.persistence.model.Word;
//import io.github.solomkinmv.glossary.persistence.model.WordSet;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.quiz.context.SpringBootTest;
//import org.springframework.quiz.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.PersistenceException;
//import javax.validation.ConstraintViolationException;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static org.junit.Assert.*;
//
///**
// * Test for {@link WordSetDaoJpaDao}
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//public class StudiedWordSetServiceJpaDaoTest {
//    @Autowired
//    private WordSetDao wordSetDao;
//
//    @Autowired
//    private WordDao wordDao;
//
//    @Test
//    public void savesWordSet() {
//        String name = "name";
//        String description = "description";
//        WordSet wordSet = new WordSet(name, description, Collections.emptyList());
//
//        WordSet savedWordSet = wordSetDao.update(wordSet);
//
//        assertNotNull(savedWordSet.getId());
//        assertNotNull(savedWordSet.getStudiedWords());
//        assertEquals(name, savedWordSet.getName());
//        assertEquals(description, savedWordSet.getDescription());
//    }
//
//    @Test
//    public void savesWordSetWithNullWords() {
//        String name = "name";
//        String description = "description";
//        WordSet wordSet = new WordSet(name, description, null);
//
//        WordSet savedWordSet = wordSetDao.update(wordSet);
//
//        assertNotNull(savedWordSet.getId());
//        assertNull(savedWordSet.getStudiedWords());
//        assertEquals(name, savedWordSet.getName());
//        assertEquals(description, savedWordSet.getDescription());
//    }
//
//    @Test
//    public void updatesWordSet() {
//        String name = "name";
//        String description = "description";
//        String updatedDescription = "description 2";
//        WordSet wordSet = new WordSet(name, description, Collections.emptyList());
//
//        WordSet savedWordSet = wordSetDao.update(wordSet);
//        savedWordSet.setDescription(updatedDescription);
//        WordSet updatedWordSet = wordSetDao.update(savedWordSet);
//
//        assertNotNull(updatedWordSet.getId());
//        assertNotNull(updatedWordSet.getStudiedWords());
//        assertEquals(name, updatedWordSet.getName());
//        assertEquals(updatedDescription, updatedWordSet.getDescription());
//    }
//
//    @Test
//    public void getsById() {
//        String name = "name";
//        String description = "description";
//        WordSet wordSet = new WordSet(name, description, Collections.emptyList());
//
//        WordSet savedWordSet = wordSetDao.update(wordSet);
//
//        Optional<WordSet> foundWordSet = wordSetDao.findOne(savedWordSet.getId());
//
//        assertTrue(foundWordSet.isPresent());
//        assertEquals(savedWordSet, foundWordSet.orElse(null));
//    }
//
//    @Test
//    public void deletesWordSet() {
//        String name = "name";
//        String description = "description";
//        WordSet wordSet = new WordSet(name, description, Collections.emptyList());
//
//        WordSet savedWordSet = wordSetDao.update(wordSet);
//        Optional<WordSet> foundWordSet = wordSetDao.findOne(savedWordSet.getId());
//
//        wordSetDao.delete(foundWordSet.orElse(null).getId());
//
//        Optional<WordSet> absentWordSet = wordSetDao.findOne(savedWordSet.getId());
//
//        assertFalse(absentWordSet.isPresent());
//    }
//
//    @Test
//    public void deletesAllWithoutWords() {
//        wordSetDao.update(new WordSet("text", "description", null));
//        wordSetDao.update(new WordSet("text", "description", null));
//        wordSetDao.update(new WordSet("text", "description", null));
//
//        wordSetDao.deleteAll();
//
//        assertTrue(wordSetDao.listAll().isEmpty());
//    }
//
//    @Test
//    public void deletesAllWithWords() {
//        List<StudiedWord> words1 = getWords();
//        List<StudiedWord> words2 = getWords();
//        words2.add(new StudiedWord("text", "translation"));
//
//        wordSetDao.update(new WordSet("text", "description", words1));
//        wordSetDao.update(new WordSet("text", "description", words2));
//
//        wordSetDao.deleteAll();
//
//        assertTrue(wordSetDao.listAll().isEmpty());
//        assertEquals(words1.size() + words2.size(), wordDao.listAll().size());
//    }
//
//    @Test
//    public void deletesWordFromWordSet() {
//        List<StudiedWord> studiedWords = getWords();
//
//        String name = "name";
//        String description = "description";
//        WordSet wordSet = new WordSet(name, description, studiedWords);
//
//        WordSet savedWordSet = wordSetDao.update(wordSet);
//        studiedWords = new ArrayList<>(savedWordSet.getStudiedWords()); // saved studiedWords
//
//        StudiedWord studiedWordToDelete = new ArrayList<>(studiedWords).get(0);
//        savedWordSet.getStudiedWords().remove(studiedWordToDelete);
//
//        WordSet updatedWordSet = wordSetDao.update(savedWordSet);
//
//        assertNotNull(updatedWordSet.getId());
//        assertEquals(name, updatedWordSet.getName());
//        assertEquals(description, updatedWordSet.getDescription());
//        assertNotNull(updatedWordSet.getStudiedWords());
//        assertEquals(1, updatedWordSet.getStudiedWords().size());
//        assertEquals(studiedWords.size(), wordDao.listAll().size());
//    }
//
//    // delete don't delete anything. Why?
//    @Ignore
//    @Test(expected = PersistenceException.class)
//    public void deletesWordWhichIsInWordSet() {
//        List<StudiedWord> studiedWords = getWords();
//
//        String name = "name";
//        String description = "description";
//        WordSet wordSet = new WordSet(name, description, studiedWords);
//
//        wordSetDao.create(wordSet);
//        StudiedWord studiedWordToDelete = wordSet.getStudiedWords().get(0);
//
//        wordDao.delete(studiedWordToDelete.getId());
//    }
//
//    // delete don't delete anything. Why?
//    @Ignore
//    @Test(expected = PersistenceException.class)
//    public void deletesAllWordsWhichIsInWordSet() {
//        List<StudiedWord> studiedWords = getWords();
//
//        String name = "name";
//        String description = "description";
//        WordSet wordSet = new WordSet(name, description, studiedWords);
//
//        wordSetDao.update(wordSet);
//
//        wordDao.deleteAll();
//    }
//
//    @Test(expected = ConstraintViolationException.class)
//    public void failesToAddWordSetWithNullName() {
//        wordSetDao.update(new WordSet(null, "text", Collections.emptyList()));
//    }
//
//    @Test(expected = ConstraintViolationException.class)
//    public void failesToAddWordSetWithNullDescription() {
//        wordSetDao.update(new WordSet("text", null, Collections.emptyList()));
//    }
//
//    @Test
//    public void savesWordSetWithWords() {
//        List<StudiedWord> studiedWords = getWords();
//
//        String name = "name";
//        String description = "description";
//        WordSet wordSet = new WordSet(name, description, studiedWords);
//
//        WordSet savedWordSet = wordSetDao.update(wordSet);
//
//        assertNotNull(savedWordSet.getId());
//        assertEquals(name, savedWordSet.getName());
//        assertEquals(description, savedWordSet.getDescription());
//        assertEquals(studiedWords.size(), wordDao.listAll().size());
//    }
//
//    @Test
//    public void addsWordToWordSet() {
//        List<StudiedWord> studiedWords = getWords();
//
//        String name = "name";
//        String description = "description";
//        WordSet wordSet = new WordSet(name, description, studiedWords);
//
//        WordSet savedWordSet = wordSetDao.update(wordSet);
//
//        StudiedWord studiedWordToAdd = new StudiedWord("added word", "added word translation");
//
//        savedWordSet.getStudiedWords().add(studiedWordToAdd);
//
//        WordSet updatedWordSet = wordSetDao.update(savedWordSet);
//
//        assertNotNull(savedWordSet.getId());
//        assertEquals(name, savedWordSet.getName());
//        assertEquals(description, savedWordSet.getDescription());
//        assertEquals(studiedWords.size() + 1, wordDao.listAll().size());
//
//        StudiedWord addedStudiedWord = updatedWordSet.getStudiedWords().get(studiedWords.size());
//
//        assertNotNull(addedStudiedWord.getId());
//        assertEquals("added word", addedStudiedWord.getText());
//        assertEquals("added word translation", addedStudiedWord.getTranslation());
//    }
//
//    @Test
//    public void assignWordsToWordSet() {
//        List<StudiedWord> studiedWords = getStudiedWords();
//
//        String name = "name";
//        String description = "description";
//        WordSet wordSet = new WordSet(name, description, Collections.emptyList());
//
//        WordSet savedWordSet = wordSetDao.update(wordSet);
//        studiedWords.stream().map(word -> wordDao.update(word))
//                                                          .collect(Collectors.toList());
//
//        savedWordSet.getStudiedWords().addAll(savedStudiedWords);
//
//        WordSet updatedWordSet = wordSetDao.update(savedWordSet);
//
//        assertNotNull(updatedWordSet.getId());
//        assertEquals(name, updatedWordSet.getName());
//        assertEquals(description, updatedWordSet.getDescription());
//        assertEquals(savedStudiedWords.size(), updatedWordSet.getStudiedWords().size());
//        assertEquals(savedStudiedWords.get(0), updatedWordSet.getStudiedWords().get(0));
//        assertEquals(savedStudiedWords.get(1), updatedWordSet.getStudiedWords().get(1));
//    }
//
//    private List<Word> getWords() {
//        return new ArrayList<>(Arrays.asList(
//                new Word("word1", "translation1"),
//                new Word("word2", "translation2")
//                                            ));
//    }
//
//    private List<StudiedWord> getStudiedWords() {
//        return getWords().stream()
//                         .map(StudiedWord::new)
//                         .collect(Collectors.toList());
//    }
//}