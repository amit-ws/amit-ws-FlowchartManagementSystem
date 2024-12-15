package com.conceptile.service;

import com.conceptile.dto.request.CreateFlowchartRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.entity.Flowchart;
import com.conceptile.entity.User;
import com.conceptile.exception.NoDataFoundException;
import com.conceptile.mapper.FlowChartMgmtGlobalMapper;
import com.conceptile.repository.FlowchartRepository;
import com.conceptile.repository.NodeConnectionRepository;
import com.conceptile.repository.NodeRepository;
import com.conceptile.repository.UserRepository;
import com.conceptile.util.GenericUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
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
public class FlowchartService {
    final UserRepository userRepository;
    final FlowchartRepository flowchartRepository;
    final NodeRepository nodeRepository;
    final NodeConnectionRepository nodeConnectionRepository;
    final FlowChartMgmtGlobalMapper flowChartMgmtGlobalMapper;

    @Autowired
    public FlowchartService(UserRepository userRepository, FlowchartRepository flowchartRepository, NodeRepository nodeRepository, NodeConnectionRepository nodeConnectionRepository, FlowChartMgmtGlobalMapper flowChartMgmtGlobalMapper) {
        this.userRepository = userRepository;
        this.flowchartRepository = flowchartRepository;
        this.nodeRepository = nodeRepository;
        this.nodeConnectionRepository = nodeConnectionRepository;
        this.flowChartMgmtGlobalMapper = flowChartMgmtGlobalMapper;
    }


    @Transactional
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
    public void deleteFlowchartAndAssociatedNodesAndConnections(Long flowchartId) {
        GenericUtil.ensureNotNull(flowchartId, "Flowchart id not provided");
        flowchartRepository.deleteByFlowChartId(flowchartId);
    }


    public Map<String, Boolean> validateTheFlowchart(Long flowchartId) {
        GenericUtil.ensureNotNull(flowchartId, "Please provide flowchartId");
        Flowchart flowchart = flowchartRepository.findByFlowChartId(flowchartId).orElseThrow(() -> new NoDataFoundException("No flowchart found with provided id: " + flowchartId));
        Long uniqueConnectedNodes = nodeConnectionRepository.countUniqueNodesForFlowchartWhichAreConnected(flowchartId);
        Long totalCreatedNodes = nodeRepository.countAllByFlowchart(flowchart);
        return Collections.singletonMap("status", uniqueConnectedNodes.equals(totalCreatedNodes));
    }
}
