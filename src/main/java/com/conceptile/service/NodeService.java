package com.conceptile.service;

import com.conceptile.dto.request.CreateNodeRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.entity.Flowchart;
import com.conceptile.entity.Node;
import com.conceptile.entity.User;
import com.conceptile.exception.NoDataFoundException;
import com.conceptile.mapper.FlowChartMgmtGlobalMapper;
import com.conceptile.repository.FlowchartRepository;
import com.conceptile.repository.NodeRepository;
import com.conceptile.repository.UserRepository;
import com.conceptile.util.GenericUtil;
import com.conceptile.util.PositionNodeUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NodeService {
    final UserRepository userRepository;
    final FlowchartRepository flowchartRepository;
    final NodeRepository nodeRepository;
    final FlowChartMgmtGlobalMapper mapper;

    @Autowired
    public NodeService(UserRepository userRepository, FlowchartRepository flowchartRepository, NodeRepository nodeRepository, FlowChartMgmtGlobalMapper mapper) {
        this.userRepository = userRepository;
        this.flowchartRepository = flowchartRepository;
        this.nodeRepository = nodeRepository;
        this.mapper = mapper;
    }

    @Transactional
    public FlowchartDTO createNodesForFlowchart(Long userId, Long flowchartId, List<CreateNodeRequest> nodeRequests) {
        GenericUtil.ensureNotNull(userId, "Please provide user id");
        GenericUtil.ensureNotNull(flowchartId, "Please provide flowchart id");
        GenericUtil.ensureListNotEmpty(nodeRequests, "Node creation payload not provided");
        User foundUser = userRepository.findByUserId(userId).orElseThrow(() -> new NoDataFoundException("No user found with provided user id: " + userId));
        Flowchart flowchart = flowchartRepository.findByFlowChartId(flowchartId).orElseThrow(() -> new NoDataFoundException("No flowchart found with provided id: " + flowchartId));
        List<Node> lastSavedNodes = nodeRepository.findAllByFlowchart(flowchart);
        List<Node> nodes = nodeRequests.stream()
                .map(nodeRequest -> {
                    Pair<Integer, Integer> pairOfAxis = (nodeRequest.getXAxis() == null && nodeRequest.getYAxis() == null)
                            ? PositionNodeUtil.generatePosition(lastSavedNodes)
                            : Pair.of(nodeRequest.getXAxis(), nodeRequest.getYAxis());
                    return Node.builder()
                            .name(nodeRequest.getNodeName().trim())
                            .type(nodeRequest.getNodeType())
                            .description(Optional.ofNullable(nodeRequest.getNodeDescription()).map(String::trim).orElse(null))
                            .xAxis(pairOfAxis.getLeft())
                            .yAxis(pairOfAxis.getRight())
                            .createdAt(LocalDateTime.now())
                            .flowchart(flowchart)
                            .user(foundUser)
                            .build();
                })
                .collect(Collectors.toList());
        FlowchartDTO flowchartDTO = mapper.fromFlowchartEntityToFlowchartDTO(flowchart);
        flowchartDTO.setNodes(
                mapper.fromNodeEntitiesToNodeDTOs(nodeRepository.saveAll(nodes))
        );
        return flowchartDTO;
    }
}

















