package com.conceptile.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
}
