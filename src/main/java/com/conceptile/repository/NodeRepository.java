package com.conceptile.repository;


import com.conceptile.entity.Flowchart;
import com.conceptile.entity.Node;
import com.conceptile.entity.NodeConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    List<Node> findAllByFlowchart(Flowchart flowchart);

    Long countAllByFlowchart(Flowchart flowchart);

    void deleteByFlowchartAndNodeId(Flowchart flowchart, Long nodeId);
}
