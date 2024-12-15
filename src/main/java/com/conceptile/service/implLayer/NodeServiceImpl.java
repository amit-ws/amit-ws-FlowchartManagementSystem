package com.conceptile.service.implLayer;

import com.conceptile.dto.request.CreateNodeRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.entity.Flowchart;
import com.conceptile.entity.Node;
import com.conceptile.entity.User;
import com.conceptile.exception.NoDataFoundException;
import com.conceptile.mapper.FlowChartMgmtGlobalMapper;
import com.conceptile.repository.FlowchartRepository;
import com.conceptile.repository.NodeConnectionRepository;
import com.conceptile.repository.NodeRepository;
import com.conceptile.repository.UserRepository;
import com.conceptile.service.innterfaceLayer.NodeService;
import com.conceptile.util.GenericUtil;
import com.conceptile.util.PositionNodeUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NodeServiceImpl implements NodeService {
    final UserRepository userRepository;
    final FlowchartRepository flowchartRepository;
    final NodeRepository nodeRepository;
    final NodeConnectionRepository nodeConnectionRepository;
    final FlowChartMgmtGlobalMapper mapper;

    @Autowired
    public NodeServiceImpl(UserRepository userRepository, FlowchartRepository flowchartRepository, NodeRepository nodeRepository, NodeConnectionRepository nodeConnectionRepository, FlowChartMgmtGlobalMapper mapper) {
        this.userRepository = userRepository;
        this.flowchartRepository = flowchartRepository;
        this.nodeRepository = nodeRepository;
        this.nodeConnectionRepository = nodeConnectionRepository;
        this.mapper = mapper;
    }

    @Override
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


    @Override
    @Transactional
    public void deleteNodeAndConnections(Long flowchartId, Long nodeId) {
        GenericUtil.ensureNotNull(flowchartId, "Please provide flowchart ID for which node(s) has to be deleted");
        GenericUtil.ensureNotNull(nodeId, "Please provide node ID(s) to delete");
        Flowchart flowchart = flowchartRepository.findByFlowChartId(flowchartId).orElseThrow(() -> new NoDataFoundException("No flowchart found with provided ID: " + flowchartId));
        nodeConnectionRepository.deleteAllConnectionsFROMandTOnodes(flowchartId, nodeId);
        nodeRepository.deleteByFlowchartAndNodeId(flowchart, nodeId);
    }
}

















