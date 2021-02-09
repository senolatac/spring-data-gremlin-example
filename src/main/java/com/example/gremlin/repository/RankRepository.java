package com.example.gremlin.repository;

import com.example.gremlin.model.KeywordRanksAppRelation;
import com.example.gremlin.repository.support.SimpleGremlinRepository;
import org.springframework.stereotype.Component;

/**
 * @author sa
 * @date 9.02.2021
 * @time 17:57
 */
@Component
public class RankRepository extends SimpleGremlinRepository<KeywordRanksAppRelation> implements IRankRepository
{
}
