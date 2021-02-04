package com.example.gremlin.domain;

import com.microsoft.spring.data.gremlin.annotation.Edge;
import com.microsoft.spring.data.gremlin.annotation.EdgeFrom;
import com.microsoft.spring.data.gremlin.annotation.EdgeTo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author sa
 * @date 3.02.2021
 * @time 12:04
 */
@Data
@Edge
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Relation {

    @Id
    private String id;

    private String name;

    @EdgeFrom
    private Person personFrom;

    @EdgeTo
    private Person personTo;
}
