package io.github.solomkinmv.glossary.web.resource;

import io.github.solomkinmv.glossary.service.statistics.Statistic;
import io.github.solomkinmv.glossary.web.controller.StatisticController;
import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class StatisticResource extends ResourceSupport {
    private final Statistic statistic;

    public StatisticResource(Statistic statistic) {
        this.statistic = statistic;

        add(linkTo(methodOn(StatisticController.class).getStatistic(null)).withSelfRel());
    }
}
