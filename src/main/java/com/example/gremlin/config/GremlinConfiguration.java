package com.example.gremlin.config;

import com.microsoft.spring.data.gremlin.common.GremlinConfig;
import com.microsoft.spring.data.gremlin.config.AbstractGremlinConfiguration;
import com.microsoft.spring.data.gremlin.repository.config.EnableGremlinRepositories;
import org.apache.tinkerpop.gremlin.driver.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author sa
 * @date 3.02.2021
 * @time 11:50
 */
@Configuration
@EnableGremlinRepositories(basePackages = "com.example.gremlin")
@EnableConfigurationProperties(GremlinProperties.class)
@PropertySource("classpath:gremlin.yml")
public class GremlinConfiguration extends AbstractGremlinConfiguration
{
    @Autowired
    private GremlinProperties gremlinProps;

    @Override
    public GremlinConfig getGremlinConfig()
    {
       return GremlinConfig.defaultBuilder()
               .endpoint(gremlinProps.getEndpoint())
               .port(gremlinProps.getPort())
               .sslEnabled(gremlinProps.isSslEnabled())
               .serializer(gremlinProps.getSerializer())
               .telemetryAllowed(gremlinProps.isTelemetryAllowed())
               .maxContentLength(gremlinProps.getMaxContentLength())
               .build();
    }
}
