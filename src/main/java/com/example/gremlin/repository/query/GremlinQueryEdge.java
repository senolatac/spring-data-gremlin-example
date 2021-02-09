package com.example.gremlin.repository.query;

import com.example.gremlin.conversion.source.GremlinSourceEdgeReader;
import com.example.gremlin.model.IModel;
import com.example.gremlin.util.ModelUtils;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.example.gremlin.util.Constants.*;

/**
 * @author sa
 * @date 9.02.2021
 * @time 12:53
 */
@Component
@NoArgsConstructor
public class GremlinQueryEdge<E extends IModel> extends AbstractGremlinQuery<E> implements IGremlinQuery<E>
{
    private GremlinSourceEdgeReader sourceEdgeReader = new GremlinSourceEdgeReader();

    @Override
    public Object save(E model)
    {
        GraphTraversal graphTraversal = graph.addE(label);
        Map<String, Object> fields = ModelUtils.findNonNullPersistentFieldsOfModel(model);
        for (var field : fields.entrySet())
        {
            graphTraversal.property(field.getKey(), field.getValue());
        }
        return graphTraversal.next();
    }

    @Override
    public void deleteById(String id)
    {
        graph.E(id).drop();
    }

    @Override
    public List<E> findById(String id)
    {
        final List<String> queryList = generateFindByIdScript(id);
        final List<Result> results = this.executeQuery(queryList);

        if (results.isEmpty())
        {
            return null;
        }
        return sourceEdgeReader.read(domainClass, results);
    }

    @Override
    public List<E> findAll()
    {
        return findByFields(null);
    }

    @Override
    public List<E> findByFields(Map<String, Object> fields)
    {
        final List<String> queryList = generateFindByFields(fields);
        final List<Result> results = this.executeQuery(queryList);

        if (results.isEmpty())
        {
            return null;
        }
        return sourceEdgeReader.read(domainClass, results);
    }

    private List<String> generateFindByIdScript(@NonNull String id)
    {
        final List<String> scriptList = Arrays.asList(
                GREMLIN_PRIMITIVE_GRAPH,                                 // g
                GREMLIN_PRIMITIVE_VERTEX_ALL,                            // E()
                generateHasId(id),                                       // hasId(xxx)
                GREMLIN_PRIMITIVE_ELEMENT_MAP                            // elementMap()
        );

        return completeScript(scriptList);
    }

    private List<String> generateFindByFields(Map<String, Object> fields)
    {
        List<String> scriptList = new ArrayList<>();
        scriptList.addAll(Arrays.asList(
                GREMLIN_PRIMITIVE_GRAPH,
                GREMLIN_PRIMITIVE_EDGE_ALL,
                generateHasLabel(label)
        ));
        if (!CollectionUtils.isEmpty(fields))
        {
            generatePropertyConditions(scriptList, fields);
        }
        scriptList.add(GREMLIN_PRIMITIVE_ELEMENT_MAP);
        return completeScript(scriptList);
    }
}
