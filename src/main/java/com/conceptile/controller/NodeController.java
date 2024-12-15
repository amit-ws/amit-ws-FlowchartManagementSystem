package com.conceptile.controller;

import com.conceptile.dto.request.CreateNodeRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.exception.ErrorDetailResponse;
import com.conceptile.exception.NoDataFoundException;
import com.conceptile.service.innterfaceLayer.NodeService;
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
@RequestMapping("/api/nodes")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NodeController {
    final NodeService nodeService;

    @Autowired
    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }


    @Operation(
            summary = "Create Nodes",
            description = "Create nodes for a flowchart",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successful Nodes creation",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlowchartDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @PostMapping("/v1/create")
    public ResponseEntity<FlowchartDTO> createNodesHandler(@RequestParam Long userId, @RequestParam Long flowchartId, @Valid @RequestBody List<CreateNodeRequest> requests) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nodeService.createNodesForFlowchart(userId, flowchartId, requests));
        } catch (NoDataFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Delete Node and its connections",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Node and connections deleted"),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorDetailResponse.class)))
            }
    )
    @DeleteMapping("/v1/delete")
    public ResponseEntity<FlowchartDTO> deleteNodeAndConnectionHandler(@RequestParam Long flowchartId, @RequestParam Long nodeId) {
        try {
            nodeService.deleteNodeAndConnections(flowchartId, nodeId);
            return ResponseEntity.noContent().build();
        } catch (NoDataFoundException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }


}
