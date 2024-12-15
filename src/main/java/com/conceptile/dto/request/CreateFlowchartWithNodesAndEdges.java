package com.conceptile.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateFlowchartWithNodesAndEdges {
    @NotNull(message = "Please provide user id")
    Long userId;

    /* Flowchart entity related*/
    @NotNull(message = "Please provide flowchart related details eg: type, description")
    CreateFlowchartRequest flowchart;

    /* Node entity related*/
    @NotEmpty(message = "Please provide flowchart node(s)")
    List<CreateNodeRequest> nodes;
}
