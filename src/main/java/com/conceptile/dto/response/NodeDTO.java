package com.conceptile.dto.response;

import com.conceptile.constant.NodeType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class NodeDTO {
    Long nodeId;
    String name;
    NodeType type;
    String description;
    Integer xAxis;
    Integer yAxis;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
