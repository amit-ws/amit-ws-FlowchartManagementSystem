package com.conceptile.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateFlowchartRequest {
    @NotNull(message = "Flowchart title is required")
    String title;
    String description;
}
