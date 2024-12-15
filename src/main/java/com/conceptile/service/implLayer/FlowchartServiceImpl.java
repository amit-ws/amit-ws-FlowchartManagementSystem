package com.conceptile.service.implLayer;

import com.conceptile.dto.request.CreateFlowchartRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.dto.response.FlowchartValidityResponse;
import com.conceptile.dto.response.NodeDTO;
import com.conceptile.entity.Flowchart;
import com.conceptile.entity.User;
import com.conceptile.exception.NoDataFoundException;
import com.conceptile.mapper.FlowChartMgmtGlobalMapper;
import com.conceptile.repository.FlowchartRepository;
import com.conceptile.repository.NodeConnectionRepository;
import com.conceptile.repository.NodeRepository;
import com.conceptile.repository.UserRepository;
import com.conceptile.service.innterfaceLayer.FlowchartService;
import com.conceptile.util.GenericUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlowchartServiceImpl implements FlowchartService {
    final UserRepository userRepository;
    final FlowchartRepository flowchartRepository;
    final NodeRepository nodeRepository;
    final NodeConnectionRepository nodeConnectionRepository;
    final FlowChartMgmtGlobalMapper flowChartMgmtGlobalMapper;
    final FlowChartMgmtGlobalMapper mapper;

    @Autowired
    public FlowchartServiceImpl(UserRepository userRepository, FlowchartRepository flowchartRepository, NodeRepository nodeRepository, NodeConnectionRepository nodeConnectionRepository, FlowChartMgmtGlobalMapper flowChartMgmtGlobalMapper, FlowChartMgmtGlobalMapper mapper) {
        this.userRepository = userRepository;
        this.flowchartRepository = flowchartRepository;
        this.nodeRepository = nodeRepository;
        this.nodeConnectionRepository = nodeConnectionRepository;
        this.flowChartMgmtGlobalMapper = flowChartMgmtGlobalMapper;
        this.mapper = mapper;
    }


    @Transactional
    @Override
    public List<FlowchartDTO> createFlowchart(Long userId, List<CreateFlowchartRequest> requests) {
        GenericUtil.ensureNotNull(userId, "UserId not provided");
        GenericUtil.ensureListNotEmpty(requests, "Payload not provided");
        User foundUser = userRepository.findByUserId(userId).orElseThrow(() -> new NoDataFoundException("No user found with provided userId: " + userId));
        List<Flowchart> flowcharts = requests.stream()
                .map(request -> Flowchart.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .createdAt(LocalDateTime.now())
                        .user(foundUser)
                        .build())
                .collect(Collectors.toList());
        return flowChartMgmtGlobalMapper.fromFlowchartEntitiesToFlowchartDTOs(flowchartRepository.saveAll(flowcharts));
    }


    @Transactional
    @Override
    public void deleteFlowchartAndAssociatedNodesAndConnections(Long flowchartId) {
        GenericUtil.ensureNotNull(flowchartId, "Flowchart id not provided");
        flowchartRepository.deleteByFlowChartId(flowchartId);
    }


    @Override
    public FlowchartValidityResponse validateTheFlowchart(Long flowchartId) {
        GenericUtil.ensureNotNull(flowchartId, "Please provide flowchartId");
        Flowchart flowchart = flowchartRepository.findByFlowChartId(flowchartId).orElseThrow(() -> new NoDataFoundException("No flowchart found with provided id: " + flowchartId));
        Long totalCreatedNodes = nodeRepository.countAllByFlowchart(flowchart);
        if (totalCreatedNodes == null || totalCreatedNodes == 0) {
            return FlowchartValidityResponse.builder()
                    .status(null)
                    .message("No nodes found. Please try creating some nodes")
                    .build();
        }
        Long uniqueConnectedNodes = nodeConnectionRepository.countUniqueNodesForFlowchartWhichAreConnected(flowchartId);
        String message = uniqueConnectedNodes.equals(totalCreatedNodes) ? "Flowchart is valid" : "Flowchart is Invalid";
        return FlowchartValidityResponse.builder()
                .status(uniqueConnectedNodes.equals(totalCreatedNodes))
                .message(message)
                .build();
    }

    @Override
    public FlowchartDTO getFlowchartWithNodesAndConnections(Long userId, Long flowchartId) throws NoDataFoundException, IllegalArgumentException {
        GenericUtil.ensureNotNull(userId, "UserId not provided");
        GenericUtil.ensureNotNull(flowchartId, "Flowchart ID not provided");
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NoDataFoundException("No user found with provided IU: " + userId));
        Flowchart flowchart = flowchartRepository.findByFlowChartIdAndUser(flowchartId, user).orElseThrow(() -> new NoDataFoundException(String.format("No flowchart found for provided ID: %s and User ID: %s ", flowchartId, userId)));
        FlowchartDTO dto = mapper.fromFlowchartEntityToFlowchartDTO(flowchart);
        dto.setNodes(mapper.fromNodeEntitiesToNodeDTOs(flowchart.getNodes()));
        dto.setNodeConnections(mapper.fromNodeConnectionsToNodeConnectionDTO(nodeConnectionRepository.findAllByFlowchart(flowchart)));
        return dto;
    }
}
