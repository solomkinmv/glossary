package io.github.solomkinmv.glossary.web.controller;

import io.github.solomkinmv.glossary.web.model.Foo;
import io.github.solomkinmv.glossary.web.service.FooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/foos")
public class FooController {

    private final FooService fooService;

    @Autowired
    public FooController(FooService fooService) {
        this.fooService = fooService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Foo> findAll() {
        return fooService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Foo findOne(@PathVariable("id") Long id) {
        return fooService.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") Long id, @RequestBody Foo resource) {
        // check if exist
        fooService.update(resource);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        fooService.deleteById(id);
    }
}
