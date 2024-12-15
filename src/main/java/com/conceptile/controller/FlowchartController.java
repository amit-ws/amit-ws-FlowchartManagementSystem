package com.conceptile.controller;

import com.conceptile.dto.request.CreateFlowchartRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.exception.ErrorDetailResponse;
import com.conceptile.exception.NoDataFoundException;
import com.conceptile.service.innterfaceLayer.FlowchartService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<FlowchartDTO>> createFlowchartHandler(@RequestParam Long userId, @Valid @RequestBody List<CreateFlowchartRequest> request) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(flowchartService.createFlowchart(userId, request));
        } catch (NoDataFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Delete flowchart by ID",
            description = "Delete a flowchart and associated nodes, connections (edges) using flowchartID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User deleted",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @DeleteMapping("/v1/deleteById")
    public ResponseEntity deleteByIdHandler(@RequestParam Long flowChartId) {
        try {
            flowchartService.deleteFlowchartAndAssociatedNodesAndConnections(flowChartId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw e;
        }
    }


    @Operation(
            summary = "Validate Flowchart by checking all missing nodes connections",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Flowchart is valid",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @GetMapping("/v1/isValid")
    public ResponseEntity<Map<String, Boolean>> validateTheFlowchartHandler(@RequestParam Long flowChartId) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(flowchartService.validateTheFlowchart(flowChartId));
        } catch (NoDataFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}
