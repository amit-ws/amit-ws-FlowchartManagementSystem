package com.conceptile.service;

import com.conceptile.dto.request.CreateNodeConnectionRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.dto.response.NodeConnectionDTO;
import com.conceptile.entity.Flowchart;
import com.conceptile.entity.Node;
import com.conceptile.entity.NodeConnection;
import com.conceptile.exception.NoDataFoundException;
import com.conceptile.mapper.FlowChartMgmtGlobalMapper;
import com.conceptile.repository.FlowchartRepository;
import com.conceptile.repository.NodeConnectionRepository;
import com.conceptile.repository.NodeRepository;
import com.conceptile.util.GenericUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NodeConnectionService {
    final FlowchartRepository flowchartRepository;
    final NodeRepository nodeRepository;
    final NodeConnectionRepository nodeConnectionRepository;
    final FlowChartMgmtGlobalMapper mapper;

    @Autowired
    public NodeConnectionService(FlowchartRepository flowchartRepository, NodeRepository nodeRepository, NodeConnectionRepository nodeConnectionRepository, FlowChartMgmtGlobalMapper mapper) {
        this.flowchartRepository = flowchartRepository;
        this.nodeRepository = nodeRepository;
        this.nodeConnectionRepository = nodeConnectionRepository;
        this.mapper = mapper;
    }


    @Transactional
    public FlowchartDTO createNodesConnections(Long flowchartId, List<CreateNodeConnectionRequest> requests) {
        GenericUtil.ensureNotNull(flowchartId, "Flowchart id not provided");
        GenericUtil.ensureListNotEmpty(requests, "Payload not provided");

        Flowchart flowchart = flowchartRepository.findByFlowChartId(flowchartId).orElseThrow(() -> new NoDataFoundException("Flowchart not found with provided id: " + flowchartId));
        Set<Long> nodeIdsToValidate = requests.stream()
                .flatMap(request -> Stream.of(request.getFromNodeId(), request.getToNodeId()))
                .collect(Collectors.toSet());

        Map<Long, Node> nodeMap = nodeRepository.findAllById(nodeIdsToValidate).stream()
                .collect(Collectors.toMap(Node::getNodeId, Function.identity()));
        GenericUtil.ensureMapNotEmpty(nodeMap, "No nodes found for the requested payload");

        List<NodeConnection> nodeConnections = requests.stream()
                .map(request -> {
                    Node fromNode = nodeMap.get(request.getFromNodeId());
                    Node toNode = nodeMap.get(request.getToNodeId());
                    if (fromNode == null || toNode == null) {
                        throw new NoDataFoundException(String.format(
                                "Nodes with IDs %d and %d were not found",
                                request.getFromNodeId(),
                                request.getToNodeId()
                        ));
                    }
                    return NodeConnection.builder()
                            .type(request.getType())
                            .condition(request.getCondition() != null ? request.getCondition().trim() : null)
                            .fromNode(fromNode)
                            .toNode(toNode)
                            .flowchart(flowchart)
                            .createdAt(LocalDateTime.now())
                            .build();
                })
                .collect(Collectors.toList());
        FlowchartDTO flowchartDTO = mapper.fromFlowchartEntityToFlowchartDTO(flowchart);
        flowchartDTO.setNodeConnections(fromNodeConnectionsToNodeConnectionDTO(nodeConnectionRepository.saveAll(nodeConnections)));
        flowchartDTO.setNodes(mapper.fromNodeEntitiesToNodeDTOs(nodeRepository.findAllByFlowchart(flowchart)));
        return flowchartDTO;
    }


    @Transactional
    public void deleteNodeConnection(Long flowChartId, List<Long> connectionIds) {
        GenericUtil.ensureNotNull(flowChartId, "Please provide flowchart ID");
        GenericUtil.ensureListNotEmpty(connectionIds, "Please provide connections IDs to remove");
        Flowchart flowchart = flowchartRepository.findByFlowChartId(flowChartId).orElseThrow(() -> new NoDataFoundException("No flowchart found with provided id: " + flowChartId));
        nodeConnectionRepository.deleteAllByFlowchartAndConnectionIdIn(flowchart, connectionIds);
    }

    private List<NodeConnectionDTO> fromNodeConnectionsToNodeConnectionDTO(List<NodeConnection> connections) {
        return connections.stream()
                .map(nodeConnection -> NodeConnectionDTO.builder()
                        .connectionId(nodeConnection.getConnectionId())
                        .type(nodeConnection.getType())
                        .condition(nodeConnection.getCondition())
                        .createdAt(nodeConnection.getCreatedAt())
                        .updatedAt(nodeConnection.getUpdatedAt())
                        .fromNodeName(nodeConnection.getFromNode().getName())
                        .toNodeName(nodeConnection.getToNode().getName())
                        .build())
                .collect(Collectors.toList());
    }
}
















