package com.conceptile.controller;

import com.conceptile.dto.request.CreateNodeConnectionRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.exception.ErrorDetailResponse;
import com.conceptile.service.NodeConnectionService;
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

@RestController
@RequestMapping("/api/nodeConnections")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NodeConnectionController {
    final NodeConnectionService nodeConnectionService;

    @Autowired
    public NodeConnectionController(NodeConnectionService nodeConnectionService) {
        this.nodeConnectionService = nodeConnectionService;
    }

    @Operation(
            summary = "Create Nodes Connections (edges)",
            description = "Connect nodes of a flowchart (edges)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful node connections",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlowchartDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @PostMapping("/v1/create")
    public ResponseEntity<FlowchartDTO> createNodeConnectionsHandler(@RequestParam Long flowchartId, @Valid @RequestBody List<CreateNodeConnectionRequest> requests) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nodeConnectionService.createNodesConnections(flowchartId, requests));
    }
}
