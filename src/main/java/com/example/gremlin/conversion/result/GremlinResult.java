package com.example.gremlin.conversion.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author sa
 * @date 9.02.2021
 * @time 10:33
 */
@Getter
@Setter
@NoArgsConstructor
public class GremlinResult
{
    private String id;
    private Map<String, Object> properties;

    //Only for edges.
    private String fromEdgeId;
    private String toEdgeId;
}
