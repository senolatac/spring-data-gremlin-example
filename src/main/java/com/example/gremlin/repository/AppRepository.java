package com.example.gremlin.repository;

import com.example.gremlin.model.App;
import com.example.gremlin.model.Country;
import com.example.gremlin.model.Keyword;
import com.example.gremlin.repository.support.SimpleGremlinRepository;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/**
 * @author sa
 * @date 8.02.2021
 * @time 17:13
 */
@Component
public class AppRepository extends SimpleGremlinRepository<App> implements IAppRepository
{
    @Override
    public void calculateVisibilityScores(String[] popularityCountries, LocalDate visibilityScoreUpdateDate, Integer skipCount, Integer limit)
    {
        graph.V().hasLabel(App.LABEL).as("a")
                .order().by("trackId")
                //.range(skipCount, limit)
                .skip(skipCount)
                .limit(limit)
                .inE("RANKS").as("r")
                .outV().hasLabel(Keyword.LABEL).as("k")
                .choose(__.select("k").by("countryCode").is(__.in(popularityCountries)),
                        __.math("1.0 / r * k * k").by("rank").by("popularity").by("popularity"),
                        __.math("1.0 / r * k * k").by("rank").by("searchVolume").by("searchVolume"))
                .as("vis_score")
                .select("a")
                .V().hasLabel(Country.LABEL)
                .has("countryCode", __.select("k").by("countryCode")).as("c")
                //If it's not exist, add edge
                .coalesce(
                        __.outE("VISIBLE_ON")
                                .where(__.inV().as("a"))
                                .where(__.outV().as("c")),
                        __.addE("VISIBLE_ON").to("c").from("a")
                )
                .select("a")
                .outE("VISIBLE_ON").as("v")
                .property("visibilityScore", __.select("vis_score"))
                .inV()
                .hasLabel(Country.LABEL)
                .has("countryCode", __.select("k").by("countryCode"))
                .select("a")
                .property("totalVisibilityScore", __.math("100"))
                .property("visibilityScoreUpdateDate", visibilityScoreUpdateDate)
                .select("a").tryNext();
    }

    @Override
    public App findByTrackId(Long trackId)
    {
        Optional<Map<Object, Object>> valueMap = graph.V().has(App.LABEL, "trackId", trackId)
                .valueMap().with(WithOptions.tokens).tryNext();
        if (valueMap.isPresent())
        {
            return App.builder()
                    .id(String.valueOf(valueMap.get().get(T.id)))
                    .trackId((Long) getProperty(valueMap.get(), "trackId"))
                    .build();
        }
        else
        {
            return null;
        }
    }

    private Object getProperty(Map m, Object key)
    {
        ArrayList value = ((ArrayList) m.get(key));
        return CollectionUtils.isEmpty(value) ? null : value.get(0);
    }
}
