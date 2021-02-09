package com.example.gremlin.model;

import com.example.gremlin.annotation.Id;
import com.example.gremlin.annotation.Property;
import com.example.gremlin.annotation.Vertex;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * @author sa
 * @date 5.02.2021
 * @time 10:25
 */
@Vertex(label = Keyword.LABEL)
@Data
@NoArgsConstructor
public class Keyword implements IModel
{
    public static final String LABEL = "KEYWORD";

    @Id
    private String id;

    @Property
    private String value;

    @Property
    private String countryCode;

    @Property
    private LocalDate rankingsUpdateDate;

    @Property
    private Integer popularity;

    @Property
    private LocalDate popularityUpdateDate;

    @Property
    private Integer searchVolume;

    @Property
    private LocalDate searchVolumeUpdateDate;

    @Property
    private Double difficultyScore;

    @Property
    private LocalDate brandAppUpdateDate;

    private List<KeywordRanksAppRelation> rankings;

    @Property
    private String nodeKey;

    @Property
    private LocalDate paidBysUpdateDate;
}
