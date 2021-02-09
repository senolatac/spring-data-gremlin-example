package com.example.gremlin.repository.support;

import com.example.gremlin.model.IModel;

import java.util.List;
import java.util.Map;

/**
 * @author sa
 * @date 9.02.2021
 * @time 12:06
 */
public interface IGremlinRepository<T extends IModel>
{
    List<T> findById(final String id);
    Object save(final T entity);

    List<T> findAll();

    List<T> findByFields(Map<String, Object> fields);

    void deleteById(final String id);
}
