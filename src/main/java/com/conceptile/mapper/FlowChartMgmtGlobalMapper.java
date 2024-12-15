package com.conceptile.mapper;

import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.dto.response.NodeDTO;
import com.conceptile.dto.response.UserDTO;
import com.conceptile.entity.Flowchart;
import com.conceptile.entity.Node;
import com.conceptile.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlowChartMgmtGlobalMapper {
    UserDTO fromUserEntityToUserDTO(User userEntity);

    FlowchartDTO fromFlowchartEntityToFlowchartDTO(Flowchart flowchartEntity);

    NodeDTO fromNodeEntityToNodeDTO(Node nodeEntity);

    List<FlowchartDTO> fromFlowchartEntitiesToFlowchartDTOs(List<Flowchart> flowcharts);

    List<NodeDTO> fromNodeEntitiesToNodeDTOs(List<Node> nodeEntities);

}
