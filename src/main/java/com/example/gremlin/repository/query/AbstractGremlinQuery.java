package com.example.gremlin.repository.query;

import lombok.Setter;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.example.gremlin.util.Constants.*;
import static java.util.stream.Collectors.toList;

/**
 * @author sa
 * @date 9.02.2021
 * @time 13:44
 */
public abstract class AbstractGremlinQuery<E> implements IGremlinQuery<E>
{
    @Autowired
    protected Client gremlinClient;

    @Autowired
    protected GraphTraversalSource graph;

    @Setter
    public Class<E> domainClass;

    @Setter
    public String label;

    protected List<Result> executeQuery(@NonNull List<String> queries)
    {
        return executeQueryParallel(queries);
    }

    private List<Result> executeQueryParallel(@NonNull List<String> queries)
    {
        return queries.parallelStream()
                .map(q -> gremlinClient.submit(q).all())
                .collect(toList()).parallelStream().flatMap(f -> {
                    try
                    {
                        return f.get().stream();
                    }
                    catch (InterruptedException | ExecutionException e)
                    {
                        throw new IllegalStateException("unable to complete query from gremlin", e);
                    }
                })
                .collect(toList());
    }

    protected static List<String> completeScript(@NonNull List<String> scriptList)
    {
        return Collections.singletonList(String.join(GREMLIN_PRIMITIVE_INVOKE, scriptList));
    }

    protected static String generateHasId(@NonNull Object id)
    {
        if (id instanceof String)
        {
            return String.format("hasId('%s')", id);
        }
        else if (id instanceof Integer)
        {
            return String.format("hasId(%d)", (Integer) id);
        }
        else if (id instanceof Long)
        {
            return String.format("hasId(%d)", (Long) id);
        }
        else
        {
            throw new IllegalStateException("the type of @Id/id field should be String/Integer/Long");
        }
    }

    protected static String generateHasLabel(@NonNull String label)
    {
        return String.format("hasLabel('%s')", label);
    }

    private static String generateHas(@NonNull String name, @NonNull Integer value)
    {
        return String.format(GREMLIN_PRIMITIVE_HAS_NUMBER, name, value);
    }

    private static String generateHas(@NonNull String name, @NonNull Boolean value)
    {
        return String.format(GREMLIN_PRIMITIVE_HAS_BOOLEAN, name, value);
    }

    private static String generateHas(@NonNull String name, @NonNull String value)
    {
        return String.format(GREMLIN_PRIMITIVE_HAS_STRING, name, value);
    }

    protected static String generateHas(@NonNull String name, @NonNull Object value)
    {

        if (value instanceof Integer)
        {
            return generateHas(name, (Integer) value);
        }
        else if (value instanceof Boolean)
        {
            return generateHas(name, (Boolean) value);
        }
        else if (value instanceof String)
        {
            return generateHas(name, (String) value);
        }
        else
        {
            throw new IllegalStateException("Illegal comparison");
        }
    }

    protected void generatePropertyConditions(List<String> scriptList, Map<String, Object> fields)
    {
        for (var field : fields.entrySet())
        {
            if (field.getKey().equalsIgnoreCase(PROPERTY_ID))
            {
                scriptList.add(generateHasId(field.getValue()));
            }
            else
            {
                scriptList.add(generateHas(field.getKey(), field.getValue()));
            }
            scriptList.add(GREMLIN_PRIMITIVE_AND);
        }
        scriptList.remove(scriptList.size() - 1); //remove last and operation
    }
}
