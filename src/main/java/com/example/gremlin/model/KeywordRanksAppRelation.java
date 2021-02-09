package com.example.gremlin.model;

import com.example.gremlin.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sa
 * @date 8.02.2021
 * @time 16:48
 */
@Edge(label = KeywordRanksAppRelation.LABEL)
@Data
@NoArgsConstructor
public class KeywordRanksAppRelation implements IModel
{
    public static final String LABEL = "RANKS";

    @Id
    private String id;

    @Property
    private Integer rank;

    @EdgeFrom
    private Keyword keyword;

    @EdgeTo
    private App app;
}
