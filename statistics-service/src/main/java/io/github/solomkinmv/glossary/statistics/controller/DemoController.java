package io.github.solomkinmv.glossary.statistics.controller;

import io.github.solomkinmv.glossary.statistics.service.DemoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@AllArgsConstructor
public class DemoController {

    private final DemoService demoService;

    @PostMapping("/")
    public void publish(@RequestParam("number") int number) {
        demoService.send(number);
    }
}
