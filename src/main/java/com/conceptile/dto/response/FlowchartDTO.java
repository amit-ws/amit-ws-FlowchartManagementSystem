package com.conceptile.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FlowchartDTO {
    Long flowChartId;
    String title;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<NodeDTO> nodes = new ArrayList<>();
    List<NodeConnectionDTO> nodeConnections = new ArrayList<>();
}
