package com.example.gremlin.service;

import com.example.gremlin.model.App;
import com.example.gremlin.model.KeywordRanksAppRelation;
import com.example.gremlin.repository.IAppRepository;
import com.example.gremlin.repository.IRankRepository;
import com.example.gremlin.util.NumberUtils;
import lombok.RequiredArgsConstructor;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sa
 * @date 5.02.2021
 * @time 10:46
 */
@Service
@RequiredArgsConstructor
public class AppService implements IAppService
{
    private final GraphTraversalSource graph;
    private final Client gremlinClient;
    private final IAppRepository appRepository;
    private final IRankRepository rankRepository;

    @Override
    public void saveApp(App app)
    {
        appRepository.save(app);
    }

    @Override
    public App findByTrackId(Long trackId)
    {
        return appRepository.findByTrackId(trackId);
    }

    @Override
    public List<App> findById(String id)
    {
        return appRepository.findById(id);
    }

    @Override
    public void dropGraph()
    {
        graph.V().drop();
    }

    @Override
    public void saveAppNetwork()
    {
        String app1Id = NumberUtils.randomNumber();
        String app2Id = NumberUtils.randomNumber();
        String app3Id = NumberUtils.randomNumber();

        String keyword1Id = NumberUtils.randomNumber();
        String keyword2Id = NumberUtils.randomNumber();
        String keyword3Id = NumberUtils.randomNumber();

        String country1Id = NumberUtils.randomNumber();
        String country2Id = NumberUtils.randomNumber();

        saveDummyApps(app1Id, app2Id, app3Id);
        saveDummyKeywords(keyword1Id, keyword2Id, keyword3Id);
        saveDummyCountries(country1Id, country2Id);
        saveDummyRanks(app1Id, app2Id, app3Id, keyword1Id, keyword2Id, keyword3Id);
        saveDummyVisibilities(app1Id, app2Id, country1Id, country2Id);
    }

    @Override
    public List<KeywordRanksAppRelation> getAllEdges()
    {
        return rankRepository.findAll();
    }

    @Override
    public void getEdge()
    {
        try
        {
            String query = "g.E().hasLabel('RANKS').elementMap()";

            ResultSet resultSet = gremlinClient.submit(query);

            List<Result> results = resultSet.all().get();
            for (Result result : results)
            {
                //System.out.println(result.getEdge());
                //System.out.println(result.getEdge().property("rank"));
                //System.out.println(result.getEdge().inVertex());
                final Map<Object, Object> map = (Map<Object, Object>) result.getObject();
                //final Map<String, Object> properties = (Map<String, Object>) map.get(PROPERTY_PROPERTIES);
                for (var m : map.entrySet())
                {
                    System.out.println(m.getKey().getClass());
                    System.out.println(m.getValue());
                }
                System.out.println(map.keySet());
                System.out.println(map.get(Direction.OUT));
                System.out.println(map.get(Direction.IN));
                Map<String, Object> inMap = (Map<String, Object>) map.get(Direction.IN);
                System.out.println(inMap.get(T.id));
                System.out.println(map.get("rank"));
                System.out.println(map.get(T.id));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<App> getAllApps()
    {
        /*
        List<App> apps = new ArrayList<>();
        List<Map<Object,Object>> vmaps = graph.V().hasLabel("IosApp").limit(10).skip(0).valueMap().toList();

        for (Map<Object, Object> m : vmaps)
        {
            App app = new App();
            //app.setId((Long) ((ArrayList) m.get("id")).get(0));
            app.setTrackId((Long) ((ArrayList) m.get("trackId")).get(0));
            app.setTotalVisibilityScore((Double) ((ArrayList) m.get("totalVisibilityScore")).get(0));
            apps.add(app);
        }
        return apps;

         */
        return appRepository.findAll();
    }

    @Override
    public List<App> getAllAppsWithClient()
    {
        try
        {
            String query = "g.V().hasLabel('IosApp').limit(10).skip(0).valueMap().with(WithOptions.tokens)";

            ResultSet resultSet = gremlinClient.submit(query);

            List<Result> results = resultSet.all().get();
            for (Result result : results)
            {
                final Map<String, Object> map = (Map<String, Object>) result.getObject();
                //final Map<String, Object> properties = (Map<String, Object>) map.get(PROPERTY_PROPERTIES);
                System.out.println(map.keySet());
                System.out.println(getProperty(map, "trackId"));
                System.out.println(map.get(T.id));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private Object getProperty(Map m, Object key)
    {
        ArrayList value = ((ArrayList) m.get(key));
        return CollectionUtils.isEmpty(value) ? null : value.get(0);
    }

    @Override
    public void calculateVisibilityScores()
    {
        String[] popularityCountries = {"US", "CA"};
        LocalDate visibilityScoreUpdateDate = LocalDate.now();
        Integer skipCount = 0;
        Integer limit = 10;

        GraphTraversal t = graph.V().hasLabel("IosApp").as("a")
                .order().by("trackId")
                //.range(skipCount, limit)
                .skip(skipCount)
                .limit(limit)
                .inE("RANKS").as("r")
                .outV().hasLabel("IosKeyword").as("k")
                .choose(__.select("k").by("countryCode").is(__.in(popularityCountries)),
                        __.math("1.0 / r * k * k").by("rank").by("popularity").by("popularity"),
                        __.math("1.0 / r * k * k").by("rank").by("searchVolume").by("searchVolume"))
                .as("vis_score")
                .select("a")
                .V().hasLabel("IosCountry")
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
                .hasLabel("IosCountry")
                .has("countryCode", __.select("k").by("countryCode"))
                .select("a")
                .property("totalVisibilityScore", __.math("100"))
                .property("visibilityScoreUpdateDate", visibilityScoreUpdateDate)
                .select("a").by("totalVisibilityScore");

               /*
        .has("IosCountry", "countryCode", __.select("country")).as("c")
                .addE("VISIBLE_ON").as("v")
                .property("visibilityScore", __.select("vis_score"))
                .from(__.select("a"))
                .to(__.select("c"))
                .select("a")
                .property("totalVisibilityScore", __.math("100"))
                .property("visibilityScoreUpdateDate", visibilityScoreUpdateDate).next();

        */
        t.forEachRemaining(
                e ->  System.out.println(e)
        );

    }

    private void saveDummyApps(String app1Id, String app2Id, String app3Id)
    {
        graph.addV("IosApp")
                .property(T.id, app1Id)//Id should be unique for all graph-vertexes
                .property("trackId", 1L)
                .next();
        graph.addV("IosApp")
                .property(T.id, app2Id)
                .property("trackId", 2L)
                .next();
        graph.addV("IosApp")
                .property(T.id, app3Id)
                .property("trackId", 3L)
                .next();
    }

    private void saveDummyKeywords(String keyword1Id, String keyword2Id, String keyword3Id)
    {
        graph.addV("IosKeyword")
                .property(T.id, keyword1Id)
                .property("value", "kw1")
                .property("countryCode", "US")
                .property("popularity", 5)
                .property("searchVolume", 5)
                .next();
        graph.addV("IosKeyword")
                .property(T.id, keyword2Id)
                .property("value", "kw2")
                .property("countryCode", "US")
                .property("popularity", 7)
                .property("searchVolume", 7)
                .next();
        graph.addV("IosKeyword")
                .property(T.id, keyword3Id)
                .property("value", "kw3")
                .property("countryCode", "CA")
                .property("popularity", 8)
                .property("searchVolume", 8)
                .next();
    }

    private void saveDummyCountries(String country1Id, String country2Id)
    {
        graph.addV("IosCountry")
                .property(T.id, country1Id)
                .property("countryCode", "US")
                .property("maxVisibilityScore", 9)
                .next();
        graph.addV("IosCountry")
                .property(T.id, country2Id)
                .property("countryCode", "CA")
                .property("maxVisibilityScore", 10)
                .next();
    }

    private void saveDummyRanks(String app1Id, String app2Id, String app3Id, String keyword1Id, String keyword2Id, String keyword3Id)
    {
        graph.addE("RANKS")
                .property("rank", 5)
                .from(graph.V(keyword1Id).hasLabel("IosKeyword"))
                .to(graph.V(app1Id).hasLabel("IosApp"))
                .next();
        graph.addE("RANKS")
                .property("rank", 4)
                .from(graph.V(keyword2Id).hasLabel("IosKeyword"))
                .to(graph.V(app1Id).hasLabel("IosApp"))
                .next();
        graph.addE("RANKS")
                .property("rank", 7)
                .from(graph.V(keyword3Id).hasLabel("IosKeyword"))
                .to(graph.V(app2Id).hasLabel("IosApp"))
                .next();
        graph.addE("RANKS")
                .property("rank", 7)
                .from(graph.V(keyword3Id).hasLabel("IosKeyword"))
                .to(graph.V(app3Id).hasLabel("IosApp"))
                .next();
    }

    private void saveDummyVisibilities(String app1Id, String app2Id, String country1Id, String country2Id)
    {
        graph.addE("VISIBLE_ON")
                .property("visibilityScore", 6)
                .from(graph.V(app1Id).hasLabel("IosApp"))
                .to(graph.V(country1Id).hasLabel("IosCountry"))
                .next();
        graph.addE("VISIBLE_ON")
                .property("visibilityScore", 7)
                .from(graph.V(app1Id).hasLabel("IosApp"))
                .to(graph.V(country2Id).hasLabel("IosCountry"))
                .next();
        graph.addE("VISIBLE_ON")
                .property("visibilityScore", 8)
                .from(graph.V(app2Id).hasLabel("IosApp"))
                .to(graph.V(country2Id).hasLabel("IosCountry"))
                .next();
    }
}
