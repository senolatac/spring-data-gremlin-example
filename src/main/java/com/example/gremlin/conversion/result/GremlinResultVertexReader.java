package com.example.gremlin.conversion.result;

import lombok.NoArgsConstructor;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author sa
 * @date 8.02.2021
 * @time 13:33
 */
@NoArgsConstructor
public class GremlinResultVertexReader
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
            gremlinResults.add(gremlinResult);
        }
        return gremlinResults;
    }

    private Map<String, Object> readProperties(Map<String, Object> allFields)
    {
        Map<String, Object> properties = new HashMap<>();
        for (var p : allFields.entrySet())
        {
            if (!Arrays.asList(T.id, T.label).contains(p.getKey()))
            {
                properties.put(p.getKey(), readProperty(p.getValue()));
            }
        }
        return properties;
    }

    private Object readProperty(@NonNull Object value) {
        Assert.isInstanceOf(ArrayList.class, value, "should be instance of ArrayList");

        final ArrayList<Object> valueList = (ArrayList<Object>) value;

        Assert.isTrue(valueList.size() == 1, "should be only 1 element in ArrayList");

        return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
    }
}
