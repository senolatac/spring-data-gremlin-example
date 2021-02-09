package com.example.gremlin.model;

import com.example.gremlin.annotation.Id;
import com.example.gremlin.annotation.Property;
import com.example.gremlin.annotation.Vertex;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * @author sa
 * @date 5.02.2021
 * @time 10:28
 */
@Vertex(label = Country.LABEL)
@Data
@NoArgsConstructor
public class Country implements IModel
{
    public static final String LABEL = "COUNTRY";

    @Id
    private String id;

    @Property
    private Double maxVisibilityScore;

    @Property
    private LocalDate maxVisibilityScoreUpdateDate;

    @Property
    private Double maxDifficultyScore;

    @Property
    private LocalDate maxDifficultyScoreUpdateDate;

    private List<AppVisibleOnCountryRelation> visibleFrom;

    @Property
    private String countryCode;
}
