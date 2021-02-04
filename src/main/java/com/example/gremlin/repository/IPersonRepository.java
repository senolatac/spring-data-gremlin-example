package com.example.gremlin.repository;

import com.example.gremlin.domain.Person;
import com.example.gremlin.projection.IAgeCount;
import com.microsoft.spring.data.gremlin.annotation.Query;
import com.microsoft.spring.data.gremlin.repository.GremlinRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sa
 * @date 3.02.2021
 * @time 12:06
 */
@Repository
public interface IPersonRepository extends GremlinRepository<Person, String>
{
    @Query("g.V().has('name', name)")
    Person findPersonByName(@Param("name") String name);

    @Query("g.V().hasLabel('Person').has('name', name)")
    List<Person> findPersonsByName(@Param("name") String name);

    @Query("g.V().hasLabel('Person').groupCount('count').by('age')")
    List<Person> groupByAge();
}
