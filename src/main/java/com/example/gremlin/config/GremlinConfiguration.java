package com.example.gremlin.config;

import lombok.RequiredArgsConstructor;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONMapper;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONVersion;
import org.apache.tinkerpop.shaded.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

/**
 * @author sa
 * @date 3.02.2021
 * @time 11:50
 */
@Configuration
@EnableConfigurationProperties(GremlinProperties.class)
@PropertySource("classpath:gremlin.yml")
@RequiredArgsConstructor
public class GremlinConfiguration
{
    private final GremlinProperties gremlinProps;

    @Bean
    public Cluster gremlinCluster()
    {
        return Cluster.build()
                .addContactPoint(gremlinProps.getEndpoint())
                .port(gremlinProps.getPort())
                .enableSsl(gremlinProps.isSslEnabled())
                .create();
    }

    @Bean
    public Client gremlinClient(Cluster gremlinCluster)
    {
        return gremlinCluster.connect();
    }

    @Bean
    public GraphTraversalSource gremlinGraph(Cluster gremlinCluster)
    {
        return traversal().withRemote(DriverRemoteConnection.using(gremlinCluster, "g"));
    }

    @Bean
    public ObjectMapper gremlinMapper()
    {
        return GraphSONMapper.build().version(GraphSONVersion.V3_0).create().createMapper();
    }
}
