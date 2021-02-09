package com.example.gremlin.model;

import com.example.gremlin.annotation.Id;
import com.example.gremlin.annotation.Property;
import com.example.gremlin.annotation.Vertex;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * @author sa
 * @date 5.02.2021
 * @time 10:17
 */
@Vertex(label = App.LABEL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class App implements IModel
{
    public static final String LABEL = "IOS_APP";

    @Id
    private String id;

    @Property
    private Double totalVisibilityScore;

    @Property
    private LocalDate visibilityScoreUpdateDate;

    @Property
    private LocalDate lastTrackedDate;

    private List<AppVisibleOnCountryRelation> visibleOn;

    @Property
    private Long trackId;
}
