package com.conceptile.dto.request;

import com.conceptile.constant.NodeConnectionType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateNodeConnectionRequest {
    @NotNull(message = "Node edge type is required")
    NodeConnectionType type;
    String condition;

    Integer fromNode;
    Integer toNode;
}
