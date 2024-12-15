package com.conceptile.controller;

import com.conceptile.dto.request.CreateFlowchartRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.exception.ErrorDetailResponse;
import com.conceptile.service.FlowchartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flowcharts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlowchartController {
    final FlowchartService flowchartService;

    @Autowired
    public FlowchartController(FlowchartService flowchartService) {
        this.flowchartService = flowchartService;
    }

    @Operation(
            summary = "Create Flowchart",
            description = "Create flowchart with basic metadata",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful creation",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlowchartDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @PostMapping("/v1/create")
    public ResponseEntity createFlowchartHandler(@Valid @RequestBody List<CreateFlowchartRequest> request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(flowchartService.createFlowchart(request));
    }
}
