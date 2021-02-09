package com.example.gremlin.repository.query;

import java.util.List;
import java.util.Map;

/**
 * @author sa
 * @date 9.02.2021
 * @time 12:52
 */
public interface IGremlinQuery<E>
{
    Object save(E model);

    void deleteById(String id);

    List<E> findById(String id);

    void setDomainClass(Class<E> domainClass);

    void setLabel(String label);

    List<E> findAll();

    List<E> findByFields(Map<String, Object> fields);
}
