package com.conceptile.dto.request;

import com.conceptile.constant.NodeType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateNodeRequest {
    @NotNull(message = "Node name is required")
    String nodeName;
    @NotNull(message = "Node type is required")
    NodeType nodeType;
    String nodeDescription;
    Integer xAxis;
    Integer yAxis;
}
