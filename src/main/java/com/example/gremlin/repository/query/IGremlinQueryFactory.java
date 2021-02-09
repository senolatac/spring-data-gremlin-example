package com.example.gremlin.repository.query;

import org.springframework.lang.NonNull;

/**
 * @author sa
 * @date 9.02.2021
 * @time 12:58
 */
public interface IGremlinQueryFactory<E>
{
    IGremlinQuery findGremlinSource(@NonNull Class<E> domainClass);
}
