package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.Word;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Test for {@link AbstractJpaDao}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WordServiceJpaDaoTest {
    @Autowired
    private WordDao wordDao;

    @Test
    public void savesWord() {
        String wordText = "text";
        String translation = "some text";
        Word word = new Word(wordText, translation);

        Word savedWord = wordDao.update(word);

        assertNotNull(savedWord.getId());
        assertEquals(wordText, savedWord.getText());
        assertEquals(translation, savedWord.getTranslation());
    }

    @Test
    public void updatesWord() {
        String wordText = "text";
        String translation = "some text";
        String updatedTranslation = "some text 2";
        Word word = new Word(wordText, translation);

        wordDao.create(word);
        word.setTranslation(updatedTranslation);

        assertNotNull(word.getId());
        assertEquals(wordText, word.getText());
        assertEquals(updatedTranslation, word.getTranslation());
    }

    @Test
    public void getsById() {
        String wordText = "text";
        String translation = "some text";
        Word word = new Word(wordText, translation);

        wordDao.create(word);

        Optional<Word> foundById = wordDao.findOne(word.getId());

        assertTrue(foundById.isPresent());
        assertEquals(word, foundById.orElse(null));
    }

    @Test
    public void deletesWord() {
        String wordText = "text";
        String translation = "some text";
        Word word = new Word(wordText, translation);

        wordDao.create(word);
        Optional<Word> foundById = wordDao.findOne(word.getId());

        wordDao.delete(foundById.orElse(null).getId());

        Optional<Word> absentWord = wordDao.findOne(word.getId());

        assertFalse(absentWord.isPresent());
    }

    @Test
    public void deletesAll() {
        wordDao.create(new Word("text1", "text1"));
        wordDao.create(new Word("text2", "text2"));

        wordDao.deleteAll();

        List<Word> words = wordDao.listAll();

        assertTrue(words.isEmpty());
    }

    @Test(expected = ConstraintViolationException.class)
    public void failesToAddWordWithNullText() {
        wordDao.update(new Word(null, "text"));
    }

    @Test(expected = ConstraintViolationException.class)
    public void failesToAddWordWithNullTranslation() {
        wordDao.update(new Word("text", null));
    }
}