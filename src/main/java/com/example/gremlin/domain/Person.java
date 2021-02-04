package com.example.gremlin.domain;

import com.microsoft.spring.data.gremlin.annotation.Vertex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author sa
 * @date 3.02.2021
 * @time 12:03
 */

@Data
@Vertex
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {

    @Id
    private String id;

    private String name;

    private Integer age;
}
