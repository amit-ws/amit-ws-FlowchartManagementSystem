package com.conceptile.service.innterfaceLayer;

import com.conceptile.dto.request.CreateNodeConnectionRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.exception.NoDataFoundException;

import java.util.List;

public interface NodeConnectionService {
    FlowchartDTO createNodesConnections(Long flowchartId, List<CreateNodeConnectionRequest> requests) throws NoDataFoundException;

    void deleteNodeConnection(Long flowChartId, List<Long> connectionIds) throws NoDataFoundException;
}
