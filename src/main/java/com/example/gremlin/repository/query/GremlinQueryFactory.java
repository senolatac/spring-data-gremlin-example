package com.example.gremlin.repository.query;

import com.example.gremlin.annotation.Edge;
import com.example.gremlin.annotation.Vertex;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author sa
 * @date 9.02.2021
 * @time 12:54
 */
@Component
@RequiredArgsConstructor
public class GremlinQueryFactory<E> implements IGremlinQueryFactory<E>
{
    private final IGremlinQuery gremlinQueryVertex;

    private final IGremlinQuery gremlinQueryEdge;

    @Override
    public IGremlinQuery findGremlinSource(@NonNull Class<E> domainClass)
    {
        final Vertex vertex = domainClass.getAnnotation(Vertex.class);
        final Edge edge = domainClass.getAnnotation(Edge.class);

        if (vertex != null)
        {
            gremlinQueryVertex.setDomainClass(domainClass);
            gremlinQueryVertex.setLabel(vertex.label());
            return gremlinQueryVertex;
        }
        else if (edge != null)
        {
            gremlinQueryEdge.setDomainClass(domainClass);
            gremlinQueryEdge.setLabel(edge.label());
            return gremlinQueryEdge;
        }
        else
        {
            throw new IllegalArgumentException("can not find model-type");
        }
    }
}
