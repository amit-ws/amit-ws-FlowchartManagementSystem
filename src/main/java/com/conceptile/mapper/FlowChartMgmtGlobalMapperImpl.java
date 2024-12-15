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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlowChartMgmtGlobalMapperImpl implements FlowChartMgmtGlobalMapper {

    @Override
    public UserDTO fromUserEntityToUserDTO(User userEntity) {
        if (userEntity == null) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.userId(userEntity.getUserId());
        userDTO.username(userEntity.getUsername());
        userDTO.password(userEntity.getPassword());
        userDTO.email(userEntity.getEmail());
        userDTO.createdAt(userEntity.getCreatedAt());
        userDTO.updatedAt(userEntity.getUpdatedAt());

        return userDTO.build();
    }

    @Override
    public FlowchartDTO fromFlowchartEntityToFlowchartDTO(Flowchart flowchartEntity) {
        if (flowchartEntity == null) {
            return null;
        }

        FlowchartDTO.FlowchartDTOBuilder flowchartDTO = FlowchartDTO.builder();

        flowchartDTO.flowChartId(flowchartEntity.getFlowChartId());
        flowchartDTO.title(flowchartEntity.getTitle());
        flowchartDTO.description(flowchartEntity.getDescription());
        flowchartDTO.createdAt(flowchartEntity.getCreatedAt());
        flowchartDTO.updatedAt(flowchartEntity.getUpdatedAt());
        flowchartDTO.nodes(fromNodeEntitiesToNodeDTOs(flowchartEntity.getNodes()));

        return flowchartDTO.build();
    }

    @Override
    public NodeDTO fromNodeEntityToNodeDTO(Node nodeEntity) {
        if (nodeEntity == null) {
            return null;
        }

        NodeDTO.NodeDTOBuilder nodeDTO = NodeDTO.builder();

        nodeDTO.nodeId(nodeEntity.getNodeId());
        nodeDTO.name(nodeEntity.getName());
        nodeDTO.type(nodeEntity.getType());
        nodeDTO.description(nodeEntity.getDescription());
        nodeDTO.createdAt(nodeEntity.getCreatedAt());
        nodeDTO.updatedAt(nodeEntity.getUpdatedAt());
        nodeDTO.xAxis(nodeEntity.getXAxis());
        nodeDTO.yAxis(nodeEntity.getYAxis());

        return nodeDTO.build();
    }

    @Override
    public List<FlowchartDTO> fromFlowchartEntitiesToFlowchartDTOs(List<Flowchart> flowcharts) {
        if (flowcharts == null) {
            return null;
        }

        List<FlowchartDTO> list = new ArrayList<FlowchartDTO>(flowcharts.size());
        for (Flowchart flowchart : flowcharts) {
            list.add(fromFlowchartEntityToFlowchartDTO(flowchart));
        }

        return list;
    }

    @Override
    public List<NodeDTO> fromNodeEntitiesToNodeDTOs(List<Node> nodeEntities) {
        if (nodeEntities == null) {
            return null;
        }

        List<NodeDTO> list = new ArrayList<NodeDTO>(nodeEntities.size());
        for (Node node : nodeEntities) {
            list.add(fromNodeEntityToNodeDTO(node));
        }

        return list;
    }


    public List<NodeConnectionDTO> fromNodeConnectionsToNodeConnectionDTO(List<NodeConnection> connections) {
        return connections.stream()
                .map(nodeConnection -> NodeConnectionDTO.builder()
                        .connectionId(nodeConnection.getConnectionId())
                        .type(nodeConnection.getType())
                        .condition(nodeConnection.getCondition())
                        .createdAt(nodeConnection.getCreatedAt())
                        .updatedAt(nodeConnection.getUpdatedAt())
                        .fromNodeName(nodeConnection.getFromNode().getName())
                        .toNodeName(nodeConnection.getToNode().getName())
                        .flowChartId(nodeConnection.getFlowchart().getFlowChartId())
                        .build())
                .collect(Collectors.toList());
    }

    public List<NodeConnectionDTO> fromNodeConnectionProjectionsToNodeConnectionDTOs(List<NodeConnectionProjection> nodeConnectionProjections) {
        return nodeConnectionProjections.stream()
                .map(connection -> NodeConnectionDTO.builder()
                        .connectionId(connection.getConnection_id())
                        .fromNodeName(connection.getFrom_node_name())
                        .toNodeName(connection.getTo_node_name())
                        .flowChartId(connection.getFlowchart_id())
                        .build())
                .collect(Collectors.toList());
    }
}
