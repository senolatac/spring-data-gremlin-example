package com.example.gremlin.repository;

import com.example.gremlin.domain.Relation;
import com.microsoft.spring.data.gremlin.repository.GremlinRepository;
import org.springframework.stereotype.Repository;

/**
 * @author sa
 * @date 3.02.2021
 * @time 12:10
 */
@Repository
public interface IRelationRepository extends GremlinRepository<Relation, String>
{
}
