package com.example.gremlin.repository.support;

import com.example.gremlin.model.IModel;
import com.example.gremlin.repository.query.IGremlinQuery;
import com.example.gremlin.repository.query.IGremlinQueryFactory;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

/**
 * @author sa
 * @date 9.02.2021
 * @time 12:04
 */
public abstract class SimpleGremlinRepository<E extends IModel> implements IGremlinRepository<E>
{
    @Autowired
    protected GraphTraversalSource graph;

    @Autowired
    protected Client gremlinClient;

    @Autowired
    private IGremlinQueryFactory<E> gremlinSourceFactory;

    protected Class<E> entityClass =
            (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public Object save(E model)
    {
        return getGremlinSource().save(model);
    }

    @Override
    public List<E> findById(String id)
    {
        return getGremlinSource().findById(id);
    }

    @Override
    public List<E> findAll()
    {
        return getGremlinSource().findAll();
    }

    @Override
    public List<E> findByFields(Map<String, Object> fields)
    {
        return getGremlinSource().findByFields(fields);
    }

    @Override
    public void deleteById(String id)
    {
        getGremlinSource().deleteById(id);
    }

    protected IGremlinQuery getGremlinSource()
    {
        return gremlinSourceFactory.findGremlinSource(entityClass);
    }
}
