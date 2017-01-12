package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.TopicDao;
import io.github.solomkinmv.glossary.persistence.model.Topic;
import io.github.solomkinmv.glossary.service.domain.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link TopicService}.
 */
@Service
public class TopicServiceImpl implements TopicService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicServiceImpl.class);

    private final TopicDao topicDao;

    @Autowired
    public TopicServiceImpl(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    @Override
    public List<Topic> listAll() {
        LOGGER.debug("Listing all topics");
        return topicDao.listAll();
    }

    @Override
    public Optional<Topic> getById(Long id) {
        LOGGER.debug("Getting topic by id: {}", id);
        return topicDao.getById(id);
    }

    @Override
    public Topic saveOrUpdate(Topic topic) {
        LOGGER.debug("Saving or updating topic: {}", topic);
        return topicDao.saveOrUpdate(topic);
    }

    @Override
    public void delete(Long id) {
        LOGGER.debug("Deleting topic with id: {}", id);
        topicDao.delete(id);
    }
}
