package com.example.gremlin.conversion.result;

import lombok.NoArgsConstructor;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.lang.NonNull;

import java.util.*;

/**
 * @author sa
 * @date 9.02.2021
 * @time 17:22
 */
@NoArgsConstructor
public class GremlinResultEdgeReader
{
    public List<GremlinResult> read(@NonNull List<Result> results)
    {
        List<GremlinResult> gremlinResults = new ArrayList<>();
        for (Result result : results)
        {
            GremlinResult gremlinResult = new GremlinResult();

            final Map<String, Object> allFields = (Map<String, Object>) result.getObject();

            gremlinResult.setId(String.valueOf(allFields.get(T.id)));
            gremlinResult.setProperties(readProperties(allFields));
            gremlinResult.setFromEdgeId(readDirectionId(allFields.get(Direction.IN)));
            gremlinResult.setToEdgeId(readDirectionId(allFields.get(Direction.OUT)));

            gremlinResults.add(gremlinResult);
        }
        return gremlinResults;
    }

    private Map<String, Object> readProperties(Map<String, Object> allFields)
    {
        Map<String, Object> properties = new HashMap<>();
        for (var p : allFields.entrySet())
        {
            if (!Arrays.asList(T.id, T.label, Direction.IN, Direction.OUT).contains(p.getKey()))
            {
                properties.put(p.getKey(), p.getValue());
            }
        }
        return properties;
    }

    //Expected format => {id=..., label=...}
    private String readDirectionId(@NonNull Object value) {
        if (value == null)
        {
            return null;
        }

        final Map<Object, Object> valueMap = (Map<Object, Object>) value;

        return String.valueOf(valueMap.get(T.id));
    }
}
