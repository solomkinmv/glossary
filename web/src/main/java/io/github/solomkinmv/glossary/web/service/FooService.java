package io.github.solomkinmv.glossary.web.service;

import io.github.solomkinmv.glossary.persistence.model.Foo;

import java.util.List;

public interface FooService {
    List<Foo> findAll();

    Foo findOne(Long id);

    void update(Foo resource);

    void deleteById(Long id);
}
