package com.example.gremlin.service;

import com.example.gremlin.model.App;
import com.example.gremlin.model.KeywordRanksAppRelation;

import java.util.List;

/**
 * @author sa
 * @date 5.02.2021
 * @time 10:47
 */
public interface IAppService
{
    void saveApp(App app);

    App findByTrackId(Long trackId);

    List<App> findById(String id);

    void dropGraph();

    void saveAppNetwork();

    List<KeywordRanksAppRelation> getAllEdges();

    void getEdge();

    List<App> getAllApps();

    List<App> getAllAppsWithClient();

    void calculateVisibilityScores();
}
