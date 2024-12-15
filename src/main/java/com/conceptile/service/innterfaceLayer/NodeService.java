package com.conceptile.service.innterfaceLayer;

import com.conceptile.dto.request.CreateNodeRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.exception.NoDataFoundException;

import java.util.List;

public interface NodeService {
    FlowchartDTO createNodesForFlowchart(Long userId, Long flowchartId, List<CreateNodeRequest> nodeRequests) throws NoDataFoundException;

    void deleteNodeAndConnections(Long flowchartId, Long nodeId) throws NoDataFoundException;
}
