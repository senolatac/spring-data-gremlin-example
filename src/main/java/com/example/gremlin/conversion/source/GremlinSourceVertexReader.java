package com.example.gremlin.conversion.source;

import com.example.gremlin.conversion.result.GremlinResult;
import com.example.gremlin.conversion.result.GremlinResultVertexReader;
import com.example.gremlin.util.GremlinUtils;
import com.example.gremlin.util.ModelUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sa
 * @date 8.02.2021
 * @time 23:38
 */
public class GremlinSourceVertexReader
{
    private GremlinResultVertexReader resultVertexReader;

    public GremlinSourceVertexReader()
    {
        resultVertexReader = new GremlinResultVertexReader();
    }

    public <E> List<E> read(@NonNull Class<E> domainClass, @NonNull List<Result> results)
    {
        List<GremlinResult> gremlinResults = resultVertexReader.read(results);
        return gremlinResults.stream().map(g -> read(domainClass, g)).collect(Collectors.toList());
    }

    private <T extends Object> T read(@NonNull Class<T> domainClass, @NonNull GremlinResult source)
    {
        final T domain = GremlinUtils.createInstance(domainClass);
        for (final Field field : FieldUtils.getAllFields(domainClass))
        {
            if (!ModelUtils.isFieldId(field))
            {
                ModelUtils.setField(domain, field, source.getProperties().get(field.getName()));
            }
            else
            {
                ModelUtils.setField(domain, field, source.getId());
            }
        }
        return domain;
    }
}
