package com.example.gremlin.service;

import com.example.gremlin.domain.Network;
import com.example.gremlin.domain.Person;
import com.example.gremlin.domain.Relation;
import com.example.gremlin.projection.IAgeCount;
import com.example.gremlin.repository.INetworkRepository;
import com.example.gremlin.repository.IPersonRepository;
import com.example.gremlin.util.NumberUtil;
import com.microsoft.spring.data.gremlin.common.GremlinFactory;
import com.microsoft.spring.data.gremlin.common.GremlinUtils;
import com.microsoft.spring.data.gremlin.conversion.script.GremlinScriptLiteral;
import com.microsoft.spring.data.gremlin.conversion.script.GremlinScriptLiteralGraph;
import com.microsoft.spring.data.gremlin.conversion.source.GremlinSource;
import lombok.RequiredArgsConstructor;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.structure.T;

/**
 * @author sa
 * @date 3.02.2021
 * @time 12:11
 */
@Service
@RequiredArgsConstructor
public class PersonService implements IPersonService
{
    private final IPersonRepository personRepository;
    private final INetworkRepository networkRepository;
    private final GremlinFactory factory;

    @Override
    public void deleteAll()
    {
        networkRepository.deleteAll();
    }

    @Override
    public void saveTestNetwork()
    {
        Person person = Person.builder()
                .id(NumberUtil.randomNumber().toString())
                .name("P")
                .age(5)
                .build();

        Person person0 = Person.builder()
                .id(NumberUtil.randomNumber().toString())
                .name("P0")
                .age(5)
                .build();

        Person person1 = Person.builder()
                .id(NumberUtil.randomNumber().toString())
                .name("P1")
                .age(5)
                .build();

        Relation relation = Relation.builder()
                .id(NumberUtil.randomNumber().toString())
                .name("coworker")
                .personFrom(person0)
                .personTo(person1)
                .build();

        Network network = Network.builder()
                .edges(Arrays.asList(relation))
                .vertexes(Arrays.asList(person, person0, person1))
                .build();

        networkRepository.save(network);
    }

    @Override
    public List<Person> findAllPersons()
    {
        List<Person> result = new ArrayList<>();
        personRepository.findAll().forEach(result::add);
        return result;
    }

    @Override
    public List<Person> findPersonByName(String name)
    {
        return personRepository.findPersonsByName(name);
    }

    //java.lang.ClassCastException: class java.util.LinkedHashMap cannot be cast to class
    @Override
    public List<Result> nativeQueryTest()
    {
        //final GremlinSource<?> source = GremlinUtils.toGremlinSource(Person.class);
        Map<String, Object> params = new HashMap<>();
        params.put("name", "P");
        final ResultSet rs = factory.getGremlinClient().submit("g.V().has('name', name)", params);
        try
        {
            List<Result> result = rs.all().get();
            for (String k : result.get(0).getVertex().keys())
            {
                System.out.println(k);
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Person> groupByAge()
    {
        return personRepository.groupByAge();
    }
}
