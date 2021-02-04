package com.example.gremlin.service;

import com.example.gremlin.domain.Person;
import com.example.gremlin.projection.IAgeCount;
import org.apache.tinkerpop.gremlin.driver.Result;

import java.util.List;

/**
 * @author sa
 * @date 3.02.2021
 * @time 12:11
 */
public interface IPersonService
{
    void deleteAll();

    void saveTestNetwork();

    List<Person> findAllPersons();

    List<Person> findPersonByName(String name);

    List<Result> nativeQueryTest();

    List<Person> groupByAge();
}
