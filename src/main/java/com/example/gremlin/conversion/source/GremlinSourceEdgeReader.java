package com.example.gremlin.conversion.source;

import com.example.gremlin.annotation.EdgeFrom;
import com.example.gremlin.annotation.EdgeTo;
import com.example.gremlin.annotation.Id;
import com.example.gremlin.conversion.result.GremlinResult;
import com.example.gremlin.conversion.result.GremlinResultEdgeReader;
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
 * @date 9.02.2021
 * @time 17:35
 */
public class GremlinSourceEdgeReader
{
    private GremlinResultEdgeReader resultEdgeReader;

    public GremlinSourceEdgeReader()
    {
        resultEdgeReader = new GremlinResultEdgeReader();
    }

    public <E> List<E> read(@NonNull Class<E> domainClass, @NonNull List<Result> results)
    {
        List<GremlinResult> gremlinResults = resultEdgeReader.read(results);
        return gremlinResults.stream().map(g -> read(domainClass, g)).collect(Collectors.toList());
    }

    private <T extends Object> T read(@NonNull Class<T> domainClass, @NonNull GremlinResult source)
    {
        final T domain = GremlinUtils.createInstance(domainClass);
        for (final Field field : FieldUtils.getAllFields(domainClass))
        {
            if (field.getAnnotation(EdgeFrom.class) != null)
            {
                final Object fromDomain = GremlinUtils.createInstance(field.getType());
                //should be single @Id annotation.
                Field fromIdField = FieldUtils.getFieldsWithAnnotation(field.getType(), Id.class)[0];

                ModelUtils.setField(fromDomain, fromIdField, source.getFromEdgeId());
                ModelUtils.setField(domain, field, fromDomain);
            }
            else if (field.getAnnotation(EdgeTo.class) != null)
            {
                final Object toDomain = GremlinUtils.createInstance(field.getType());
                //should be single @Id annotation.
                Field toIdField = FieldUtils.getFieldsWithAnnotation(field.getType(), Id.class)[0];

                ModelUtils.setField(toDomain, toIdField, source.getToEdgeId());
                ModelUtils.setField(domain, field, toDomain);
            }
            else if (!ModelUtils.isFieldId(field))
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
