package io.github.solomkinmv.glossary.web.service;

import io.github.solomkinmv.glossary.web.model.Foo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FooServiceImpl implements FooService {

    private final Map<Long, Foo> foos;

    public FooServiceImpl() {
        this.foos = new HashMap<>();
        foos.put(1L, new Foo(1L, "foo1"));
        foos.put(2L, new Foo(1L, "foo2"));
        foos.put(3L, new Foo(1L, "foo3"));
    }

    @Override
    public List<Foo> findAll() {
        return new ArrayList<>(foos.values());
    }

    @Override
    public Foo findOne(Long id) {
        return foos.get(id);
    }

    @Override
    public void update(Foo resource) {
        foos.put(resource.getId(), resource);
    }

    @Override
    public void deleteById(Long id) {
        foos.remove(id);
    }
}
