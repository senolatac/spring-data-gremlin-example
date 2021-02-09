package com.example.gremlin.model;

import com.example.gremlin.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author sa
 * @date 8.02.2021
 * @time 16:42
 */
@Edge(label = AppVisibleOnCountryRelation.LABEL)
@Data
@NoArgsConstructor
public class AppVisibleOnCountryRelation implements IModel
{
    public static final String LABEL = "VISIBLE_ON";

    @Id
    private String id;

    @Property
    private Double visibilityScore;

    @EdgeFrom
    private App app;

    @EdgeTo
    private Country country;
}
