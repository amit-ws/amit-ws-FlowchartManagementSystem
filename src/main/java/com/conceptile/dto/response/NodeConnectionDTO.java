package com.conceptile.dto.response;

import com.conceptile.constant.NodeConnectionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class NodeConnectionDTO {
    Long connectionId;
    NodeConnectionType type;
    String condition;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String fromNodeName;
    String toNodeName;
    Long flowChartId;
}
