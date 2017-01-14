package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.Word;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Test for {@link WordServiceJpaDao}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WordServiceJpaDaoTest {
    @Autowired
    private WordDao wordDao;

    @Test
    public void savesWord() {
        String wordText = "word";
        String translation = "some text";
        Word word = new Word(wordText, translation);

        Word savedWord = wordDao.saveOrUpdate(word);

        assertNotNull(savedWord.getId());
        assertEquals(wordText, savedWord.getWord());
        assertEquals(translation, savedWord.getTranslation());
    }

    @Test
    public void updatesWord() {
        String wordText = "word";
        String translation = "some text";
        String updatedTranslation = "some text 2";
        Word word = new Word(wordText, translation);

        Word savedWord = wordDao.saveOrUpdate(word);
        savedWord.setTranslation(updatedTranslation);
        Word updatedWord = wordDao.saveOrUpdate(savedWord);

        assertNotNull(savedWord.getId());
        assertEquals(wordText, updatedWord.getWord());
        assertEquals(updatedTranslation, updatedWord.getTranslation());
    }

    @Test
    public void getsById() {
        String wordText = "word";
        String translation = "some text";
        Word word = new Word(wordText, translation);

        Word savedWord = wordDao.saveOrUpdate(word);

        Optional<Word> foundById = wordDao.getById(savedWord.getId());

        assertTrue(foundById.isPresent());
        assertEquals(savedWord, foundById.orElse(null));
    }

    @Test
    public void deletesWord() {
        String wordText = "word";
        String translation = "some text";
        Word word = new Word(wordText, translation);

        Word savedWord = wordDao.saveOrUpdate(word);
        Optional<Word> foundById = wordDao.getById(savedWord.getId());

        wordDao.delete(foundById.orElse(null).getId());

        Optional<Word> absentWord = wordDao.getById(savedWord.getId());

        assertFalse(absentWord.isPresent());
    }

    @Test
    public void deletesAll() {
        wordDao.saveOrUpdate(new Word("text1", "text1"));
        wordDao.saveOrUpdate(new Word("text2", "text2"));

        wordDao.deleteAll();

        List<Word> words = wordDao.listAll();

        assertTrue(words.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void failesToAddNullWord() {
        wordDao.saveOrUpdate(null);
    }

    @Test(expected = ConstraintViolationException.class)
    public void failesToAddWordWithNullText() {
        wordDao.saveOrUpdate(new Word(null, "text"));
    }

    @Test(expected = ConstraintViolationException.class)
    public void failesToAddWordWithNullTranslation() {
        wordDao.saveOrUpdate(new Word("text", null));
    }
}