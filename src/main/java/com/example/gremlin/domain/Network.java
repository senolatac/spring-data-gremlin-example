package com.example.gremlin.domain;

import com.microsoft.spring.data.gremlin.annotation.EdgeSet;
import com.microsoft.spring.data.gremlin.annotation.Graph;
import com.microsoft.spring.data.gremlin.annotation.VertexSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sa
 * @date 3.02.2021
 * @time 12:05
 */
@Graph
@AllArgsConstructor
@Builder
public class Network {

    @Id
    private String id;

    public Network() {
        this.edges = new ArrayList<>();
        this.vertexes = new ArrayList<>();
    }

    @EdgeSet
    @Getter
    private List<Object> edges;

    @VertexSet
    @Getter
    private List<Object> vertexes;
}
