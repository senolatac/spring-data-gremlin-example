package com.example.gremlin.repository;

import com.example.gremlin.domain.Network;
import com.microsoft.spring.data.gremlin.repository.GremlinRepository;
import org.springframework.stereotype.Repository;

/**
 * @author sa
 * @date 3.02.2021
 * @time 12:09
 */
@Repository
public interface INetworkRepository extends GremlinRepository<Network, String>
{
}
