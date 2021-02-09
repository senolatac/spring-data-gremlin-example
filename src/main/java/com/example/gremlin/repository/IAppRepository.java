package com.example.gremlin.repository;

import com.example.gremlin.model.App;
import com.example.gremlin.repository.support.IGremlinRepository;

import java.time.LocalDate;

/**
 * @author sa
 * @date 8.02.2021
 * @time 17:13
 */
public interface IAppRepository extends IGremlinRepository<App>
{
    void calculateVisibilityScores(String[] popularityCountries, LocalDate visibilityScoreUpdateDate, Integer skipCount, Integer limit);

    App findByTrackId(Long trackId);
}
