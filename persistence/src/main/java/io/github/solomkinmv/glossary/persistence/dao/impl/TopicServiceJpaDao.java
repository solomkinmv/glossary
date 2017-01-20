package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.TopicDao;
import io.github.solomkinmv.glossary.persistence.model.Topic;
import org.springframework.stereotype.Repository;

/**
 * Implementation of {@link TopicDao}.
 */
@Repository
public class TopicServiceJpaDao extends AbstractJpaDao<Topic> implements TopicDao {

    public TopicServiceJpaDao() {
        setClazz(Topic.class);
    }
}
