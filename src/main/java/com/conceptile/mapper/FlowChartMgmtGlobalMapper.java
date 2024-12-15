package com.conceptile.mapper;

import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.dto.response.NodeConnectionDTO;
import com.conceptile.dto.response.NodeDTO;
import com.conceptile.dto.response.UserDTO;
import com.conceptile.entity.Flowchart;
import com.conceptile.entity.Node;
import com.conceptile.entity.NodeConnection;
import com.conceptile.entity.User;
import com.conceptile.projection.NodeConnectionProjection;

import java.util.List;

public interface FlowChartMgmtGlobalMapper {
    UserDTO fromUserEntityToUserDTO(User userEntity);

    FlowchartDTO fromFlowchartEntityToFlowchartDTO(Flowchart flowchartEntity);

    NodeDTO fromNodeEntityToNodeDTO(Node nodeEntity);

    List<FlowchartDTO> fromFlowchartEntitiesToFlowchartDTOs(List<Flowchart> flowcharts);

    List<NodeDTO> fromNodeEntitiesToNodeDTOs(List<Node> nodeEntities);

    List<NodeConnectionDTO> fromNodeConnectionsToNodeConnectionDTO(List<NodeConnection> connections);

    List<NodeConnectionDTO> fromNodeConnectionProjectionsToNodeConnectionDTOs(List<NodeConnectionProjection> nodeConnectionProjections);
}
