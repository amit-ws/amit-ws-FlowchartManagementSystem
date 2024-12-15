package com.conceptile.service.innterfaceLayer;

import com.conceptile.dto.request.CreateFlowchartRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.dto.response.FlowchartValidityResponse;
import com.conceptile.exception.NoDataFoundException;

import java.util.List;
import java.util.Map;

public interface FlowchartService {
    List<FlowchartDTO> createFlowchart(Long userId, List<CreateFlowchartRequest> requests) throws NoDataFoundException;

    void deleteFlowchartAndAssociatedNodesAndConnections(Long flowchartId);

    FlowchartValidityResponse validateTheFlowchart(Long flowchartId) throws NoDataFoundException;

    FlowchartDTO getFlowchartWithNodesAndConnections(Long userId, Long flowchartId) throws NoDataFoundException, IllegalArgumentException;
}
