package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.TopicDao;
import io.github.solomkinmv.glossary.persistence.model.Topic;
import io.github.solomkinmv.glossary.service.domain.TopicService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectAlreadyExistException;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
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
    public Topic save(Topic topic) {
        LOGGER.debug("Saving topic: {}", topic);
        if (topic.getId() != null) {
            return getById(topic.getId())
                    .map(topicDao::saveOrUpdate)
                    .orElseThrow(() -> new DomainObjectAlreadyExistException(
                            "Topic with such id is already exist: " + topic.getId()));
        }

        return topicDao.saveOrUpdate(topic);
    }

    @Override
    public Topic update(Topic topic) {
        LOGGER.debug("Updating topic: {}", topic);
        Long topicId = topic.getId();
        if (topicId == null) {
            LOGGER.error("Can't update topic with null id");
            throw new DomainObjectNotFound("Can't update topic with null id");
        }

        Optional<Topic> topicOptional = topicDao.getById(topicId);
        if (!topicOptional.isPresent()) {
            throw new DomainObjectNotFound("No topic with id " + topicId);
        }

        return topicDao.saveOrUpdate(topic);
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
