package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.TopicDao;
import io.github.solomkinmv.glossary.persistence.model.Topic;
import io.github.solomkinmv.glossary.service.domain.TopicService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link TopicService}.
 */
@Service
@Transactional
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
        return topicDao.findOne(id);
    }

    @Override
    public Topic save(Topic topic) {
        LOGGER.debug("Saving topic: {}", topic);

        topicDao.create(topic);

        return topic;
    }

    @Override
    public Topic update(Topic topic) {
        LOGGER.debug("Updating topic: {}", topic);
        Long topicId = topic.getId();
        if (topicId == null) {
            LOGGER.error("Can't update topic with null id");
            throw new DomainObjectNotFound("Can't update topic with null id");
        }

        Optional<Topic> topicOptional = topicDao.findOne(topicId);
        if (!topicOptional.isPresent()) {
            throw new DomainObjectNotFound("No topic with id " + topicId);
        }

        return topicDao.update(topic);
    }

    @Override
    public void delete(Long id) {
        LOGGER.debug("Deleting topic with id: {}", id);
        topicDao.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("Deleting all topics");
        topicDao.deleteAll();
    }
}
