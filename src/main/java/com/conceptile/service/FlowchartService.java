package com.conceptile.service;

import com.conceptile.dto.request.CreateFlowchartRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.entity.Flowchart;
import com.conceptile.exception.FlowChartMgmtException;
import com.conceptile.mapper.FlowChartMgmtGlobalMapper;
import com.conceptile.repository.FlowchartRepository;
import com.conceptile.repository.NodeConnectionRepository;
import com.conceptile.repository.NodeRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlowchartService {
    final FlowchartRepository flowchartRepository;
    final NodeRepository nodeRepository;
    final NodeConnectionRepository nodeConnectionRepository;
    final FlowChartMgmtGlobalMapper flowChartMgmtGlobalMapper;

    @Autowired
    public FlowchartService(FlowchartRepository flowchartRepository, NodeRepository nodeRepository, NodeConnectionRepository nodeConnectionRepository, FlowChartMgmtGlobalMapper flowChartMgmtGlobalMapper) {
        this.flowchartRepository = flowchartRepository;
        this.nodeRepository = nodeRepository;
        this.nodeConnectionRepository = nodeConnectionRepository;
        this.flowChartMgmtGlobalMapper = flowChartMgmtGlobalMapper;
    }


    @Transactional
    public List<FlowchartDTO> createFlowchart(List<CreateFlowchartRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            throw new FlowChartMgmtException("Payload not provided");
        }
        List<Flowchart> flowcharts = requests.stream()
                .map(request -> Flowchart.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .createdAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
//        return flowChartMgmtGlobalMapper.fromFlowchartEntitiesToFlowchartDTOs(flowchartRepository.saveAll(flowcharts));
        return null;
    }
}
